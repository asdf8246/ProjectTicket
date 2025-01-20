package ticket.rabbitMQ;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import ticket.model.dto.OrderMessage;
import ticket.model.entity.Seats;
import ticket.service.OrderService;
import ticket.service.SeatsService;
import ticket.socket.OrderDataSocket;

public class OrderConsumer {
    private static final String QUEUE_NAME = "order_queue";
    private static SeatsService seatsService = new SeatsService();
    private static OrderService orderService = new OrderService();

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // 宣告隊列
        	System.out.println("Connected to RabbitMQ...");
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Queue declared successfully...");

            // 設置處理消息的回調函數
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                // 反序列化訂單消息
                OrderMessage orderMessage = new Gson().fromJson(message, OrderMessage.class);
                System.out.println("接收到訂單消息，開始處理: " + orderMessage);

                try {
                    // 處理訂單
                    processOrder(orderMessage);

                    // 手动确认消息
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 进一步日志输出
                    System.err.println("Error processing order: " + e.getMessage());
                    // 通知前端失敗信息
                    notifyFrontend(orderMessage.getUserId(), null, "Failed");
                }
            };

            // 開始消費消息
            System.out.println("Starting consumer...");
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
            System.out.println("Consumer started successfully.");
            
            while (true) {
                // 主線程持續運行，不退出
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to RabbitMQ: " + e.getMessage());
        }
    }
	    
	    private static void processOrder(OrderMessage orderMessage) {
	        try {
	            // 1. 購票（buySeats）
	            List<Seats> seats = seatsService.buySeats(orderMessage.getEventId(), orderMessage.getSeatCategoryIds(), orderMessage.getNumSeatss());
	            if (seats.isEmpty()) {
	                // 如果座位已滿，發送錯誤通知給前端
	                System.out.println("票券已完售，無法處理訂單!");
	                notifyFrontend(orderMessage.getUserId(), null, "NoSeat");
	                return;
	            }

	            // 2. 創建訂單（addOrder）
	            String orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	            Integer orderId = orderService.addOrder(Integer.parseInt(orderMessage.getUserId()), orderMessage.getEventId(),
	                                                    orderMessage.getEventName(), orderMessage.getSeatPrices(),
	                                                    orderMessage.getNumSeatss(), orderDate);

	            // 3. 關聯座位和訂單（addOrderSeats）
	            orderService.addOrderSeats(orderId, seats);

	            // 通知前端訂單處理結果（例如透過 WebSocket）
	            notifyFrontend(orderMessage.getUserId(), orderId, "Success");
	        } catch (Exception e) {
	            e.printStackTrace();
	            // 如果處理失敗，通知前端失敗信息
	            notifyFrontend(orderMessage.getUserId(), null, "Failed");
	        }
	    }
	    
	    private static void notifyFrontend(String userId, Integer orderId, String status) {
	        // 假設有 WebSocket 可以通知前端
	    	OrderDataSocket.sendOrderData(userId, orderId, status);
	    }
}

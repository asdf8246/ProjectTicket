package com.example.demo.ticket.rabbitmq;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import com.example.demo.ticket.config.RabbitConfig;
import com.example.demo.ticket.model.dto.OrderMessage;
import com.example.demo.ticket.model.entity.Seats;
import com.example.demo.ticket.service.impl.OrderServiceImpl;
import com.example.demo.ticket.service.impl.SeatsServiceImpl;
import com.example.demo.ticket.socket.OrderDataSocketHandler;
import com.rabbitmq.client.Channel;

@Component
public class OrderConsumer {
	
	@Autowired
	private SeatsServiceImpl seatsService;
	
	@Autowired
	private OrderServiceImpl orderService;
	
	@Autowired
	private OrderDataSocketHandler orderDataSocketHandler;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RabbitListener(queues = RabbitConfig.QUEUE_NAME, ackMode = "MANUAL")
	@Retryable(maxAttempts = 0)  // 设置最大重试次数
    public void processOrder(OrderMessage orderMessage, Message message, Channel channel) {
        System.out.println("接收到訂單消息，開始處理: " + orderMessage);

        try {
            // 1. 購票（buySeats）
            List<Seats> seats = seatsService.buySeats(orderMessage.getEventId(), orderMessage.getSeatCategoryIds(), orderMessage.getNumSeatss());
            if (seats.isEmpty()) {
                // 如果座位已滿，發送錯誤通知給前端
                System.out.println("票券已完售，無法處理訂單!");
                notifyFrontend(orderMessage.getUserId(), null, "NoSeat");
                
                // 這裡拒絕訊息並不重新排隊
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                
                return;
            }
            
            // 2. 創建訂單（addOrder）
            String orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Date dateOrderDate = sdf.parse(orderDate);
            Integer orderId = orderService.addOrder(Integer.parseInt(orderMessage.getUserId()), orderMessage.getEventId(),
                                                    orderMessage.getEventName(), orderMessage.getSeatPrices(),
                                                    orderMessage.getNumSeatss(), dateOrderDate);

            // 3. 關聯座位和訂單（addOrderSeats）
            orderService.addOrderSeats(orderId, seats);

            // 通知前端訂單處理結果（例如透過 WebSocket）
            notifyFrontend(orderMessage.getUserId(), orderId, "Success");
            
            // 訊息處理成功後確認
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("錯誤訊息: " + e.getMessage());
            // 如果處理失敗，通知前端失敗信息
            notifyFrontend(orderMessage.getUserId(), null, "Failed");
            
            // 處理錯誤時拒絕訊息並重新排隊
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void notifyFrontend(String userId, Integer orderId, String status) {
        // 假設有 WebSocket 可以通知前端
        orderDataSocketHandler.sendOrderData(userId, orderId, status);
    }
}

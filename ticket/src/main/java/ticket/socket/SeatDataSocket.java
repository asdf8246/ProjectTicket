package ticket.socket;

import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import ticket.model.dto.SeatCategoriesDto;
import ticket.service.SeatCategoriesService;

@ServerEndpoint("/seatDataSocket")
public class SeatDataSocket {
	
	// 當 WebSocket 連線建立時觸發
    @OnOpen
    public void onOpen(Session session) {
        String sessionId = session.getId(); // 假設您使用 session.getId() 作為唯一識別
        WebSocketManager.addSession(null, sessionId, session, "seatData"); // 添加座位數據會話
        System.out.println("WebSocket connected for seat data: " + sessionId);
    }

    // 當收到前端訊息時觸發
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message from client: " + message);
    }

    // 當 WebSocket 連線關閉時觸發
    @OnClose
    public void onClose(Session session) {
        String userId = session.getId();
        WebSocketManager.removeSession(userId, "seatData"); // 移除座位數據會話
        System.out.println("WebSocket disconnected for seat data: " + userId);
    }

    // 當 WebSocket 出現錯誤時觸發
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
    
    public static void sendUpdatedData(String eventId) {
    	SeatCategoriesService seatCategoriesService = new SeatCategoriesService();
    	List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategoriesChart(eventId);
    	
    	// 構建 JSON 資料
        JsonArrayBuilder categoriesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder numSeatsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder soldSeatsBuilder = Json.createArrayBuilder();

        // 遍歷 SeatCategoriesDto 並構建 JSON 資料
        for (SeatCategoriesDto seatCategory : seatCategoriesDto) {
            String categoryName = seatCategory.getCategoryName();
            int numSeats = seatCategory.getNumSeats();
            int soldSeats = seatCategory.getSoldSeats();  // 直接使用已銷售的座位數

            // 將資料添加到 JSON 物件中
            categoriesBuilder.add(categoryName);
            numSeatsBuilder.add(numSeats);
            soldSeatsBuilder.add(soldSeats);
        }

        // 組合 JSON 資料
        JsonObject data = Json.createObjectBuilder()
                .add("categories", categoriesBuilder.build())
                .add("numSeats", numSeatsBuilder.build())
                .add("soldSeats", soldSeatsBuilder.build())
                .build();
        
        // 向所有連線的客戶端發送資料
        WebSocketManager.sendMessageToAll(data.toString(), "seatData"); // 使用 WebSocketManager 發送座位數據
    }

}
package com.example.demo.ticket.socket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.ticket.model.dto.SeatCategoriesDto;
import com.example.demo.ticket.service.impl.SeatCategoriesServiceImpl;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Component
public class SeatDataSocketHandler extends TextWebSocketHandler {
	
	@Autowired
	private SeatCategoriesServiceImpl seatCategoriesService;
	

    // 當 WebSocket 連線建立時
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        WebSocketManager.addSession(null, sessionId, session, "seatData");
        System.out.println("WebSocket connected for seat data: " + sessionId);
    }

    // 當收到前端訊息時
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message from client: " + message.getPayload());
    }

    // 當 WebSocket 連線關閉時
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = session.getId();
        WebSocketManager.removeSession(userId, "seatData");
        System.out.println("WebSocket disconnected for seat data: " + userId);
    }

    // 處理 WebSocket 出現錯誤時
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
    }

    // 用來發送座位數據
    public void sendUpdatedData(String eventId) {
        
        List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategoriesChart(eventId);

        // 構建 JSON 資料
        JsonArrayBuilder categoriesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder numSeatsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder soldSeatsBuilder = Json.createArrayBuilder();

        // 遍歷 SeatCategoriesDto 並構建 JSON 資料
        for (SeatCategoriesDto seatCategory : seatCategoriesDto) {
            String categoryName = seatCategory.getCategoryName();
            int numSeats = seatCategory.getNumSeats();
            int soldSeats = seatCategory.getSoldSeats();

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
        WebSocketManager.sendMessageToAll(data.toString(), "seatData");
    }
}

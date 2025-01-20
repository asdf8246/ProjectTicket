package com.example.demo.ticket.socket;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Component
public class OrderDataSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        System.out.println("WebSocket connected for order data: " + sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();
        try {
            JSONObject jsonMessage = new JSONObject(message.getPayload());
            String action = jsonMessage.getString("action");

            if ("register".equals(action)) {
                // 從消息中獲取自定義 ID
                Integer userId = jsonMessage.getInt("userId");
                WebSocketManager.addSession(userId.toString(), sessionId, session, "orderData");
                System.out.println("Registered userId " + userId + " for session " + sessionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        WebSocketManager.removeSession(sessionId, "orderData");
        System.out.println("WebSocket disconnected for order data: " + sessionId);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
    }

    // 用來發送訂單數據
    public void sendOrderData(String userId, Integer orderId, String orderStatus) {
        JsonObject data = null;

        if (orderId != null) {
            data = Json.createObjectBuilder()
                    .add("orderId", orderId)
                    .add("status", orderStatus)
                    .build();
        } else {
            data = Json.createObjectBuilder()
                    .add("status", orderStatus)
                    .build();
        }

        // 向指定用戶發送訂單數據
        WebSocketManager.sendMessageToUser(userId, data.toString(), "orderData");
    }
}
package ticket.socket;

import jakarta.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ServerEndpoint("/orderDataSocket")
public class OrderDataSocket {
	
	@OnOpen
    public void onOpen(Session session) {
        String sessionId = session.getId();
        System.out.println("WebSocket connected for order data: " + sessionId);
    }

    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        WebSocketManager.removeSession(sessionId, "orderData"); // 移除訂單數據會話
        System.out.println("WebSocket disconnected for order data: " + sessionId);
    }
    
    // 當收到前端訊息時觸發
    @OnMessage
    public void onMessage(String message, Session session) {
    	String sessionId = session.getId();
    	// 假設接收到的消息是 JSON 格式
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String action = jsonMessage.getString("action");

            if ("register".equals(action)) {
                // 從消息中獲取自定義 ID
                Integer userId = jsonMessage.getInt("userId");

                // 將自定義 ID 與 session.getId() 綁定
                WebSocketManager.addSession(userId.toString(), sessionId, session, "orderData");
                System.out.println("Registered userId " + userId + " for session " + sessionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 當 WebSocket 出現錯誤時觸發
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
    
    // 用來發送訂單數據給所有客戶端
    public static void sendOrderData(String userId, Integer orderId, String orderStatus) {
    	JsonObject data = null;
    	
        // 構建訂單數據的 JSON 格式
    	if ( orderId != null ) {
    		data = Json.createObjectBuilder()
                    .add("orderId", orderId)
                    .add("status", orderStatus)
                    .build();
		} else {
			data = Json.createObjectBuilder()
                    .add("status", orderStatus)
                    .build();
		}
        
        // 向所有連線的客戶端發送訂單數據
        WebSocketManager.sendMessageToUser(userId, data.toString(), "orderData"); // 使用 WebSocketManager 發送訂單數據
    }
}

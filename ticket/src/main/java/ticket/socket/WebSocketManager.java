package ticket.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.Session;

public class WebSocketManager {

    // 使用 ConcurrentHashMap 來儲存不同類型的 WebSocket 會話
    // 一個用於座位數據，一個用於訂單數據
    private static ConcurrentHashMap<String, Session> seatDataSessions = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Session> orderDataSessions = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> userIdToSessionIdMap = new ConcurrentHashMap<>();

    // 當 WebSocket 連接建立時，添加會話
    public static void addSession(String userId, String sessionId, Session session, String type) {
        if ("seatData".equals(type)) {
            seatDataSessions.put(sessionId, session);
        } else if ("orderData".equals(type)) {
            orderDataSessions.put(sessionId, session);
            userIdToSessionIdMap.put(userId, sessionId);
        }
    }

    // 當 WebSocket 連接關閉時，移除會話
    public static void removeSession(String sessionId, String type) {
        if ("seatData".equals(type)) {
            seatDataSessions.remove(sessionId);
        } else if ("orderData".equals(type)) {
        	// 根據 sessionId 查找對應的 userId
            String userId = userIdToSessionIdMap.entrySet().stream()
                                                .filter(entry -> entry.getValue().equals(sessionId))
                                                .map(Map.Entry::getKey)
                                                .findFirst()
                                                .orElse(null);  // 如果沒有找到則返回 null
            if (userId != null) {
                // 根據 sessionId 移除 userId 和 sessionId 的關聯
                userIdToSessionIdMap.remove(userId);
            }
            orderDataSessions.remove(sessionId);
        }
    }
    
 // 向指定的用戶發送消息
    public static void sendMessageToUser(String userId, String message, String type) {
        Session session = null;
        String sessionId = null;
        if ("seatData".equals(type)) {
            session = seatDataSessions.get(userId);
        } else if ("orderData".equals(type)) {
        	sessionId = userIdToSessionIdMap.get(userId);
            session = orderDataSessions.get(sessionId);
        }

        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message); // 發送消息
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 向所有活躍的 WebSocket 客戶端發送消息
    public static void sendMessageToAll(String message, String type) {
        ConcurrentHashMap<String, Session> targetSessions = null;

        if ("seatData".equals(type)) {
            targetSessions = seatDataSessions;
        } else if ("orderData".equals(type)) {
            targetSessions = orderDataSessions;
        }

        if (targetSessions != null) {
            for (Session session : targetSessions.values()) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message); // 發送消息
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

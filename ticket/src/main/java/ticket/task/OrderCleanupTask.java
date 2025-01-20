package ticket.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ticket.repository.DatabaseConnectionPool;

public class OrderCleanupTask {
    
	// 用於管理定時任務的 ScheduledExecutorService
    private static ScheduledExecutorService scheduler;

	
	public void run() {
        // 使用 try-with-resources 確保所有資源在完成後自動關閉
        String query = "SELECT order_id FROM orders WHERE order_status = 'pending' AND TIMESTAMPDIFF(MINUTE, order_date, NOW()) > 10";
        try (Connection conn = DatabaseConnectionPool.getConnection() ) {
	        try (PreparedStatement stmt = conn.prepareStatement(query);
	            ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                int orderId = rs.getInt("order_id");
	
	                // 查詢訂單對應的座位
	                String seatQuery = "SELECT seat_id FROM orders_seats WHERE order_id = ?";
	                try (PreparedStatement seatStmt = conn.prepareStatement(seatQuery)) {
	                	seatStmt.setInt(1, orderId);
		                try(ResultSet seatRs = seatStmt.executeQuery()){
		                    // 更新座位狀態
		                    while (seatRs.next()) {
		                        int seatId = seatRs.getInt("seat_id");
		                        String updateSeat = "UPDATE seats SET seat_status = 'available' WHERE seat_id = ?";
		                        try (PreparedStatement updateSeatStmt = conn.prepareStatement(updateSeat)) {
		                            updateSeatStmt.setInt(1, seatId);
		                            updateSeatStmt.executeUpdate();
		                        }
		                    }
	                    }
	                }
	
	                // 刪除過期訂單
	                String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
	                try (PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrder)) {
	                    deleteOrderStmt.setInt(1, orderId);
	                    deleteOrderStmt.executeUpdate();
	                }
	            }
	        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// 啟動定時任務
    public static void startTask() {
        // 初始化 scheduler 變數
        scheduler = Executors.newScheduledThreadPool(1);
        OrderCleanupTask task = new OrderCleanupTask();
        // 每10分鐘執行一次清理過期訂單任務
        scheduler.scheduleAtFixedRate(task::run, 0, 1, TimeUnit.MINUTES);
    }

    // 停止定時任務
    public static void stopTask() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
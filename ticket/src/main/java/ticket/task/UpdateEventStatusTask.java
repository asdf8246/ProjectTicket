package ticket.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ticket.repository.DatabaseConnectionPool;

public class UpdateEventStatusTask {
	// 用於管理定時任務的 ScheduledExecutorService
    private static ScheduledExecutorService scheduler;
    
    public void run() {
    	String sql = """ 
    					 UPDATE events
						 SET event_status = CASE 
						 WHEN sell_date > NOW() THEN '準備中'
						 WHEN sell_date <= NOW() AND event_date >= NOW() THEN '開賣中' 
						 WHEN event_date < NOW() THEN '已結束' 
						 ELSE event_status 
						 END
						 WHERE event_id > 0
					""".trim();
    	try (Connection conn = DatabaseConnectionPool.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
    			pstmt.executeUpdate();
    	} catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // 啟動定時任務
    public static void startTask() {
        // 初始化 scheduler 變數
        scheduler = Executors.newScheduledThreadPool(1);
        UpdateEventStatusTask task = new UpdateEventStatusTask();
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

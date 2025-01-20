package ticket.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ticket.rabbitMQ.OrderConsumer;
import ticket.task.OrderCleanupTask;
import ticket.task.UpdateEventStatusTask;


@WebListener
public class AppStartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		 // 獲取 Web 應用的上下文
        ServletContext context = sce.getServletContext();
        // 設置一個屬性，表示應用已經啟動
        context.setAttribute("appStatus", "started");
        
        
        // 當應用啟動時，自動啟動 OrderConsumer
        System.out.println("Tomcat started, initializing OrderConsumer...");
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OrderConsumer.main(new String[]{});  // 在這裡調用你的 OrderConsumer 來啟動它
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        consumerThread.setDaemon(true);  // 設置為守護線程，這樣Tomcat停止時會終止此線程
        consumerThread.start();  // 啟動 OrderConsumer 消費者
        
        
        // 啟動定時任務
        OrderCleanupTask.startTask();
        UpdateEventStatusTask.startTask();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// 停止定時任務
        OrderCleanupTask.stopTask();
        UpdateEventStatusTask.stopTask();
	}
}

package ticket.repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnectionPool {
	private static HikariDataSource dataSource;
	
	// 连接超时时间
    private static final long connectionTimeout = 3000;
    // 配置当前的数据库是否为只读状态
    private static final boolean readOnly = false;
    // 空闲连接超时时间
    private static final long idleTimeout = 6000;
    // 连接最大存活时间
    private static final long maxLifetime = 60000;
    // 最大连接数
    private static final int maxPoolSize = 20;
    // 最小空闲连接，默认值10，小于0或大于maximum-pool-size，都会重置为 maximum-pool-size。
    private static final int minIdle = 2; //
	
    static {
    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
    	
        // 配置連線池
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/ticket?serverTimezone=Asia/Taipei&characterEncoding=utf-8&useUnicode=true");
        config.setUsername("root");
        config.setPassword("abc123");
        
        config.setMaximumPoolSize(maxPoolSize);  // 設定最大連線數
        config.setMinimumIdle(minIdle);  // 設定最小空閒連線數
        
        config.setConnectionTimeout(connectionTimeout);
        config.setReadOnly(readOnly);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        
        config.setHealthCheckRegistry(null);
        // 初始化連線池
        dataSource = new HikariDataSource(config);
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
            throw new ExceptionInInitializerError("JDBC 驅動加載失敗");
		}
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();  // 從池中取得連線
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();  // 關閉連線池
        }
    }
}

package com.example.demo.ticket.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateEventStatusTask {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    // 每分鐘執行一次任務
    @Scheduled(cron = "0 * * * * *")
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

        try {
            // 使用 JdbcTemplate 執行 SQL 更新操作
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
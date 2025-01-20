package com.example.demo.ticket.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderCleanupTask {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;


    // 每分鐘執行一次清理過期訂單任務
    @Scheduled(cron = "0 * * * * *" )
    public void run() {
        String query = "SELECT order_id FROM orders WHERE order_status = 'pending' AND TIMESTAMPDIFF(MINUTE, order_date, NOW()) > 10";
        
        // 使用 queryForList 查詢訂單 ID，返回 List<Integer>
        List<Integer> orderIds = jdbcTemplate.queryForList(query, Integer.class);
        
        for (int orderId : orderIds) {
            // 查詢訂單對應的座位
            String seatQuery = "SELECT seat_id FROM orders_seats WHERE order_id = ?";
            List<Integer> seatIds = jdbcTemplate.queryForList(seatQuery, Integer.class, orderId);
            
            // 更新座位狀態
            for (int seatId : seatIds) {
                String updateSeat = "UPDATE seats SET seat_status = 'available' WHERE seat_id = ?";
                jdbcTemplate.update(updateSeat, seatId);
            }

            // 刪除過期訂單
            String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
            jdbcTemplate.update(deleteOrder, orderId);
        }
    }
}
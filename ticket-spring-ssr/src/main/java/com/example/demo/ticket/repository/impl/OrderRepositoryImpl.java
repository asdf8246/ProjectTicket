package com.example.demo.ticket.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.ticket.model.entity.Order;
import com.example.demo.ticket.repository.custom.OrderRepositoryCustom;


@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//取得訂單內容
	@Override
	public List<Order> getOrderSeats(Integer orderId) {
		String sql = """
						SELECT os.order_id, os.seat_id, os.category_name, os.seat_number, sc.seat_price
						FROM  orders_seats os JOIN seats s ON os.seat_id = s.seat_id
						JOIN seat_categories sc ON s.seat_category_id = sc.seat_category_id
						WHERE os.order_id = ?;
						""".trim();
		
		
		RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);
		
		return jdbcTemplate.query(sql, rowMapper, orderId);
	}
	
	
	//新增訂單關聯表內容
	@Override
	public void addOrderSeats(List<Order> orders) {
		String sql = "insert into orders_seats(order_id, seat_id, category_name, seat_number) value(?, ?, ?, ?)";
		
		List<Object[]> batchArgs = orders.stream()
		        .map(order -> new Object[] {
		            order.getOrderId(),
		            order.getSeatId(),
		            order.getCategoryName(),
		            order.getSeatNumber()
		        })
		        .collect(Collectors.toList());

		    jdbcTemplate.batchUpdate(sql, batchArgs);
		
	}

	
}

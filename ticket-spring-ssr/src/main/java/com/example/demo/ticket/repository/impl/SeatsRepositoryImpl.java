package com.example.demo.ticket.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.ticket.repository.custom.SeatsRepositoryCustom;

@Repository
public class SeatsRepositoryImpl implements SeatsRepositoryCustom {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void updateSeatStatusToReserved(Integer eventId, Integer seatCategoryId, Integer numSeats) {
        String sql = "UPDATE seats SET seat_status = 'reserved' WHERE event_id = ? AND seat_category_id = ? AND seat_status = 'available' LIMIT ?";
        
        jdbcTemplate.update(sql, eventId, seatCategoryId, numSeats);
    }
}

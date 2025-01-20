package com.example.demo.ticket.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.ticket.model.entity.Events;
import com.example.demo.ticket.repository.custom.EventRepositoryCustom;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Events> getSearchEvents(String search) {
		String sql = "select * from events where event_name like ?";

        // 使用 BeanPropertyRowMapper 自動將查詢結果映射到 Events 類別
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Events.class), "%" + search + "%");
	}

}

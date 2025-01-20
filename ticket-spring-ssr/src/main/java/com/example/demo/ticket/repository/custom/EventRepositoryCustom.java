package com.example.demo.ticket.repository.custom;

import java.util.List;

import com.example.demo.ticket.model.entity.Events;

public interface EventRepositoryCustom {
	
	List<Events> getSearchEvents(String search);
}

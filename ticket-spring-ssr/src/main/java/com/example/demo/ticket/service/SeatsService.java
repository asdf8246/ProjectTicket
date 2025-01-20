package com.example.demo.ticket.service;

import java.util.List;

import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.model.entity.Seats;

public interface SeatsService {
	
	List<Seats> buySeats(String eventId, String[] seatCategoryIds, String[] numSeatss);
	
	void updateSeatsStatus(List<OrderDto> orderSeatsDto, String seatStatus , String eventId);
}

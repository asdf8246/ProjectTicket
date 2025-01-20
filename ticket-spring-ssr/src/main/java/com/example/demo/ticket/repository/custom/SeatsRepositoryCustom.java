package com.example.demo.ticket.repository.custom;

public interface SeatsRepositoryCustom {
	
	void updateSeatStatusToReserved(Integer eventId, Integer seatCategoryId, Integer numSeats);
	
}

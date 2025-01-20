package com.example.demo.ticket.service;

import java.util.List;

import com.example.demo.ticket.model.dto.SeatCategoriesDto;

public interface SeatCategoriesService {
	
	void appendSeatCategory(Integer eventId, String[] categoryNames, String[] seatPrices, String[] numSeatss);
	
	void deleteSeatCategorie(String seatCategoryId);
	
	List<SeatCategoriesDto> getSeatCategories(String eventId);
	
	void updateSeatCategory(String eventId, String[] seatCategoryIds, String[] categoryNames, String[] seatPrices, String[] numSeatss);
	
	List<SeatCategoriesDto> getSeatCategoriesChart(String eventId);

}

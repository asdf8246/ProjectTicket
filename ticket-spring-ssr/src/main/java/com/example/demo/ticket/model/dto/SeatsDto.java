package com.example.demo.ticket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatsDto {
	private Integer seatId;
	private Integer eventId;
	private Integer seatCategoryId;
	private Integer seatNumber;
	private String seatStatus;
}

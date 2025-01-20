package com.example.demo.ticket.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private Integer orderId;
	private Integer userId;
	private Integer eventId;
	private String eventName;
	private Integer orderPrice;
	private Date orderDate;
	private String orderStatus;
	
	private Integer seatId;
	private String categoryName;
	private Integer seatNumber;
	private Integer seatPrice;
	
	private String orderDateStr;
}
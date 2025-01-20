package com.example.demo.ticket.service;

import java.util.Date;
import java.util.List;

import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.model.entity.Seats;

public interface OrderService {
	
	OrderDto getOrder(String orderId);
	
	List<OrderDto> getUserOrders(Integer userId);
	
	List<OrderDto> getOrderSeats(String orderId);
	
	Integer addOrder(Integer userId, String eventId,  String eventName, String[] seatPrices, String[] numSeatss, Date orderDate);
	
	void addOrderSeats(Integer orderId , List<Seats> seats);
	
	void deleteOrder(String orderId);
	
	void updateOrderStatus(String orderId, String orderStatus);
	
	OrderDto checkUserOrderStatus(Integer userId,String eventId);

}

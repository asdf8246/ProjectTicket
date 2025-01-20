package com.example.demo.ticket.repository.custom;

import java.util.List;

import com.example.demo.ticket.model.entity.Order;


public interface OrderRepositoryCustom {
	
	List<Order> getOrderSeats(Integer orderId);
	
	void addOrderSeats(List<Order> orders);

}

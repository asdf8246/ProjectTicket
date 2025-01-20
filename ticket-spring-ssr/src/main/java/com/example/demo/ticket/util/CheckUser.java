package com.example.demo.ticket.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.service.impl.OrderServiceImpl;

@Component
public class CheckUser {
	
	@Autowired
	private OrderServiceImpl orderService;
	
	public boolean checkOrderUser(String orderId, Integer userId) {
		
		OrderDto orderDto = orderService.getOrder(orderId);
		Integer orderUserId = orderDto.getUserId();
		if (orderUserId == userId) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkUserId(String userId, Integer certUserId) {
		if (Integer.parseInt(userId) == certUserId) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkUserRole(Integer userId , String userRole) {
		if (userRole.equals("ROLE_ADMIN")) {
			return true;
		} else {
			return false;
		}
	}
	
}

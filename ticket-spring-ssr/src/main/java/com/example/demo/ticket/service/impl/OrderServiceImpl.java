package com.example.demo.ticket.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.model.entity.Order;
import com.example.demo.ticket.model.entity.Seats;
import com.example.demo.ticket.repository.OrderRepository;
import com.example.demo.ticket.service.OrderService;


@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public OrderDto getOrder(String orderId) {
		
		return orderRepository.findById(Long.parseLong(orderId))
						.map(Order -> modelMapper.map(Order, OrderDto.class)).orElse(null);
		
	}
	
	@Override
	public List<OrderDto> getUserOrders(Integer userId){
		
		return orderRepository.getUserOrders(userId).stream()
						.map(Order -> modelMapper.map(Order, OrderDto.class))
						.collect(Collectors.toList());
		
	}
	
	@Override
	public List<OrderDto> getOrderSeats(String orderId) {
		
		return orderRepository.getOrderSeats(Integer.parseInt(orderId)).stream()
						.map(Order -> modelMapper.map(Order, OrderDto.class))
						.collect(Collectors.toList());
		
	}
	
	
	@Override
	public Integer addOrder(Integer userId, String eventId,  String eventName, String[] seatPrices, String[] numSeatss, Date orderDate) {
		Integer orderPrice = IntStream.range(0, seatPrices.length)
		        .map(i -> Integer.parseInt(seatPrices[i]) * Integer.parseInt(numSeatss[i]))
		        .sum();

		    Order order = new Order();
		    order.setUserId(userId);
		    order.setEventId(Integer.parseInt(eventId));
		    order.setEventName(eventName);
		    order.setOrderPrice(orderPrice);
		    order.setOrderDate(orderDate);
		    
		    Order saveOrder = orderRepository.save(order);
		    return saveOrder.getOrderId();
	}
	
	@Override
	public void addOrderSeats(Integer orderId , List<Seats> seats) {
		List<Order> orders = seats.stream()
		        .map(seat -> {
		            Order order = new Order();
		            order.setSeatId(seat.getSeatId());
					order.setCategoryName(seat.getSeatCategory().getCategoryName());
					order.setSeatNumber(seat.getSeatNumber());
		            order.setOrderId(orderId);  // 设置共同的 orderId
		            return order;
		        })
		        .collect(Collectors.toList());  // 收集结果为列表

		orderRepository.addOrderSeats(orders);
	}
	
	@Override
	public void deleteOrder(String orderId) {
		orderRepository.deleteById(Long.parseLong(orderId));
	}
	
	@Override
	public void updateOrderStatus(String orderId, String orderStatus) {
		orderRepository.updateOrderStatus(Integer.parseInt(orderId), orderStatus);
	}
	
	@Override
	public OrderDto checkUserOrderStatus(Integer userId,String eventId) {

		return orderRepository.checkUserOrderStatus(userId, Integer.parseInt(eventId))
						.map(Order -> modelMapper.map(Order, OrderDto.class)).orElse(null);
	}
}

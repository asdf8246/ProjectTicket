package ticket.service;

import java.util.ArrayList;
import java.util.List;

import ticket.model.dto.OrderDto;
import ticket.model.entity.Order;
import ticket.model.entity.Seats;
import ticket.repository.OrderDao;
import ticket.repository.OrderDaoImpl;

public class OrderService {
	private OrderDao orderDao = new OrderDaoImpl();
	
	public List<OrderDto> findAllOrders(String userId) {
		List<OrderDto> orderDtos = new ArrayList<OrderDto>();
		List<Order> orders = orderDao.findAllOrders(Integer.parseInt(userId));
		for (Order order : orders) {
			OrderDto orderDto = new OrderDto();
			orderDto.setOrderId(order.getOrderId());
			orderDto.setUserId(order.getUserId());
			orderDto.setEventId(order.getEventId());
			orderDto.setEventName(order.getEventName());
			orderDto.setOrderPrice(order.getOrderPrice());
			orderDto.setOrderDate(order.getOrderDate());
			orderDto.setOrderStatus(order.getOrderStatus());
			
			orderDtos.add(orderDto);
		}
		return orderDtos;
	}
	
	public OrderDto getOrder(String orderId) {
		Order order = orderDao.getOrder(Integer.parseInt(orderId));
		if (order==null) {
			return null;
		}
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderId(order.getOrderId());
		orderDto.setUserId(order.getUserId());
		orderDto.setEventId(order.getEventId());
		orderDto.setEventName(order.getEventName());
		orderDto.setOrderPrice(order.getOrderPrice());
		orderDto.setOrderDate(order.getOrderDate());
		return orderDto;
	}
	
	public List<OrderDto> getUserOrders(Integer userId){
		List<Order> orders = orderDao.getUserOrders(userId);
		List<OrderDto> orderDtos = new ArrayList<OrderDto>();
		
		for(Order order : orders) {
			OrderDto orderDto = new OrderDto();
			orderDto.setOrderId(order.getOrderId());
			orderDto.setUserId(order.getUserId());
			orderDto.setEventId(order.getEventId());
			orderDto.setEventName(order.getEventName());
			orderDto.setOrderPrice(order.getOrderPrice());
			orderDto.setOrderDate(order.getOrderDate());
			orderDto.setOrderStatus(order.getOrderStatus());
			
			orderDtos.add(orderDto);
		}
		return orderDtos;
	}
	
	public List<OrderDto> getOrderSeats(String orderId) {
		List<OrderDto> orderDtos = new ArrayList<OrderDto>();
		List<Order> orders = orderDao.getOrderSeats(Integer.parseInt(orderId));

		for(Order order : orders) {
			OrderDto orderDto = new OrderDto();
			orderDto.setOrderId(order.getOrderId());
			orderDto.setSeatId(order.getSeatId());
			orderDto.setCategoryName(order.getCategoryName());
			orderDto.setSeatNumber(order.getSeatNumber());
			orderDto.setSeatPrice(order.getSeatPrice());
			
			orderDtos.add(orderDto);
		}
		return orderDtos;
	}
	
	public Integer addOrder(Integer userId, String eventId,  String eventName, String[] seatPrices, String[] numSeatss, String orderDate) {
		Order order = new Order();
		Integer orderPrice = 0;
		for(int i=0;i<seatPrices.length;i++) {
			Integer price = Integer.parseInt(seatPrices[i]);
			Integer numSeats = Integer.parseInt(numSeatss[i]);
			
			orderPrice = (price * numSeats) + orderPrice;
		}
		order.setUserId(userId);
		order.setEventId(Integer.parseInt(eventId));
		order.setEventName(eventName);
		order.setOrderPrice(orderPrice);
		order.setOrderDate(orderDate);
		
		Integer orderId = orderDao.addOrder(order);
		return orderId;
	}
	
	public void addOrderSeats(Integer orderId , List<Seats> seats) {
		List<Order> orders = new ArrayList<Order>();
		
		for (Seats seat : seats) {
			Order order = new Order();
			order.setOrderId(orderId);
			order.setSeatId(seat.getSeatId());
			order.setCategoryName(seat.getCategoryName());
			order.setSeatNumber(seat.getSeatNumber());
			
			orders.add(order);
		}
		orderDao.addOrderSeats(orders);
	}
	
	public void deleteOrder(String orderId) {
		orderDao.deleteOrder(Integer.parseInt(orderId));
	}
	
	public void updateOrderStatus(String orderId, String orderStatus) {
		orderDao.updateOrderStatus(Integer.parseInt(orderId), orderStatus);
	}
	
	public OrderDto checkUserOrderStatus(Integer userId,String eventId) {
		Order order = orderDao.checkUserOrderStatus(userId, Integer.parseInt(eventId));
		if (order==null) {
			return null;
		}
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderId(order.getOrderId());
		orderDto.setOrderStatus(order.getOrderStatus());
		return orderDto;
	}
}

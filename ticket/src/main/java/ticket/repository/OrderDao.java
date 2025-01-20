package ticket.repository;

import java.util.List;

import ticket.model.entity.Order;

public interface OrderDao {
	
	// 取得該用戶訂單
	List<Order> findAllOrders(Integer userId);
	
	// 取得該訂單
	Order getOrder(Integer orderId);
	
	List<Order> getUserOrders(Integer userId);
	
	//取得訂單內容
	List<Order> getOrderSeats(Integer orderId);
	
	//新增訂單
	Integer addOrder(Order order);
	
	//新增訂單關聯表內容
	void addOrderSeats(List<Order> orders);
	
	//更改訂單狀態
	void updateOrderStatus(Integer orderId, String orderStatus);
	
	// 刪除
	void deleteOrder(Integer orderId);
	
	Order checkUserOrderStatus(Integer userId,Integer eventId);
}

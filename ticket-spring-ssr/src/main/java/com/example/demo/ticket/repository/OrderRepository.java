package com.example.demo.ticket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.model.entity.Order;
import com.example.demo.ticket.repository.custom.OrderRepositoryCustom;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
	
	// 取得該訂單
	/*
	Order getOrder(Integer orderId);
	
	用findById()
	*/
	
	// 取得該用戶訂單
	@Query("select o from Order o where o.userId = :userId order by o.orderDate desc")
	List<Order> getUserOrders(Integer userId);
	
	
	//新增訂單
	/*
	Integer addOrder(Order order);
	用save()
	*/
	
	
	//更改訂單狀態
	@Transactional
	@Modifying
	@Query("update Order o set o.orderStatus = :orderStatus where o.orderId = :orderId")
	void updateOrderStatus(Integer orderId, String orderStatus);
	
	// 刪除
	/*
	void deleteOrder(Integer orderId);
	
	用deleteById()
	*/
	
	@Query(value = "SELECT * FROM orders o WHERE o.user_id = :userId AND o.event_id = :eventId AND o.order_status <> 'canceled' LIMIT 1", nativeQuery = true)
	Optional<Order> checkUserOrderStatus(Integer userId,Integer eventId);
}

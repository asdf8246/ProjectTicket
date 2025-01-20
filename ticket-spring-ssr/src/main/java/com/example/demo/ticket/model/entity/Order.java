package com.example.demo.ticket.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,  -- 預訂者的用戶 ID
    event_id INT NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    order_price INT NOT NULL,  -- 實際支付的票價
    order_date DATETIME NOT NULL,
    order_status VARCHAR(255) DEFAULT 'pending',  -- 訂單狀態
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

	關聯Table
  CREATE TABLE orders_seats (
  	order_id INT NOT NULL,
  	seat_id INT NOT NULL,
  	category_name VARCHAR(255) NOT NULL,
  	seat_number INT NOT NULL,
  	PRIMARY KEY (order_id, seat_id),  -- 設置複合主鍵
  	FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
  );
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
	private Integer orderId;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "event_id", nullable = false)
	private Integer eventId;
	
	@Column(name = "event_name", nullable = false, length = 255)
	private String eventName;
	
	@Column(name = "order_price", nullable = false)
	private Integer orderPrice;
	
	@Column(name = "order_date", nullable = false)
	private Date orderDate;
	
    @Column(name = "order_status", nullable = false, length = 255)
	private String orderStatus = "pending";
	
	private Integer seatId;
	private String categoryName;
	private Integer seatNumber;
	private Integer seatPrice;
	
}

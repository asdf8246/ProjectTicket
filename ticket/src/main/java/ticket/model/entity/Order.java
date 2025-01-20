package ticket.model.entity;

import lombok.Data;

/**
 CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,  -- 預訂者的用戶 ID
    event_id INT NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    order_price INT NOT NULL,  -- 實際支付的票價
    order_date DATETIME NOT NULL,
    order_status ENUM('pending', 'paid', 'canceled') DEFAULT 'pending',  -- 訂單狀態
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
public class Order {
	private Integer orderId;
	private Integer userId;
	private Integer eventId;
	private String eventName;
	private Integer orderPrice;
	private String orderDate;
	private String orderStatus;
	
	private Integer seatId;
	private String categoryName;
	private Integer seatNumber;
	private Integer seatPrice;
	
}

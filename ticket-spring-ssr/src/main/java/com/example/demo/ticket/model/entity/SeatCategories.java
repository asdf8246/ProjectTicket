package com.example.demo.ticket.model.entity;

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
 CREATE TABLE seat_categories (
    seat_category_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,  -- 外鍵連接到活動
    category_name VARCHAR(255) NOT NULL,  -- 座位等級名稱（如：VIP、普通等）
    seat_price INT NOT NULL,  -- 票價
    num_seats INT NOT NULL,  -- 該區域有多少個座位
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE -- 外鍵約束 參照 events 的 event_id 欄位
);
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat_categories")
public class SeatCategories {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_category_id")
	private Integer seatCategoryId;
	
	@Column(name = "event_id", nullable = false)
    private Integer eventId;
	
	
	@Column(name = "category_name", nullable = false, length = 255)
	private String categoryName;
	
	@Column(name = "seat_price", nullable = false)
	private Integer seatPrice;
	
	@Column(name = "num_seats", nullable = false)
	private Integer numSeats;
}

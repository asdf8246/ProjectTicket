package com.example.demo.ticket.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 CREATE TABLE seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,  -- 外鍵連接到活動
    seat_category_id INT NOT NULL,  -- 外鍵連接到座位等級
    seat_number INT NOT NULL,  -- 座位編號，依總數量編 1~n
    seat_status VARCHAR(255) DEFAULT 'available',  -- 座位狀態
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_category_id) REFERENCES seat_categories(seat_category_id) ON DELETE CASCADE
);
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seats")
public class Seats {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
	private Integer seatId;
	
	@Column(name = "event_id", nullable = false)
	private Integer eventId;
	
	@Column(name = "seat_category_id", nullable = false)
	private Integer seatCategoryId;
	
	@Column(name = "seat_number", nullable = false)
	private Integer seatNumber;
	
    @Column(name = "seat_status", nullable = false, length = 255)
	private String seatStatus = "available";
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_category_id", insertable = false, updatable = false)
    private SeatCategories seatCategory; // 假設有 SeatCategory 實體類別
	
	
	private String categoryName;
	private Integer numSeats;

}

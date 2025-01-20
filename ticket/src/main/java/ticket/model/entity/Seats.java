package ticket.model.entity;

import lombok.Data;

/**
 CREATE TABLE seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,  -- 外鍵連接到活動
    seat_category_id INT NOT NULL,  -- 外鍵連接到座位等級
    seat_number INT NOT NULL,  -- 座位編號，依總數量編 1~n
    seat_status ENUM('available', 'reserved', 'sold') DEFAULT 'available',  -- 座位狀態
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_category_id) REFERENCES seat_categories(seat_category_id) ON DELETE CASCADE
);
 */

@Data
public class Seats {
	private Integer seatId;
	private Integer eventId;
	private Integer seatCategoryId;
	private Integer seatNumber;
	private String seatStatus;
	
	private String categoryName;
	private Integer numSeats;

}

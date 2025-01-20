package com.example.demo.ticket.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY comment '活動ID',
    event_name VARCHAR(255) NOT NULL comment '活動名稱',
    event_date DATETIME NOT NULL comment '活動日期',
    sell_date DATETIME NOT NULL comment '開賣日期',
    venue VARCHAR(255) NOT NULL comment '活動地點',
    address VARCHAR(255) NOT NULL comment '活動地址',
    description TEXT  comment '活動簡介',  -- TEXT：文本型，通常用來儲存較長的文字資料
    event_status VARCHAR(255) DEFAULT '準備中' comment '活動狀態',
    event_image LONGBLOB NOT NULL
);
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Events {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
	private Integer eventId;
	
	@Column(name = "event_name", nullable = false, length = 255)
	private String eventName;
	
	@Column(name = "event_date", nullable = false)
	private Date eventDate;
	
	@Column(name = "venue", nullable = false, length = 255)
	private String venue;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "sell_date", nullable = false)
	private Date sellDate;
	
	@Column(name = "address", nullable = false, length = 255)
	private String address;
	
	@Lob
    @Column(name = "event_image", nullable = false, columnDefinition = "LONGBLOB")
	private Byte[] eventImage;
	
    @Column(name = "event_status", nullable = false, length = 255)
	private String eventStatus = "準備中";
}

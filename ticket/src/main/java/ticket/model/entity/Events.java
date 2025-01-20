package ticket.model.entity;

import java.io.InputStream;

import lombok.Data;

/**
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY comment '活動ID',
    event_name VARCHAR(255) NOT NULL comment '活動名稱',
    event_date DATETIME NOT NULL comment '活動日期',
    sell_date DATETIME NOT NULL comment '開賣日期',
    venue VARCHAR(255) NOT NULL comment '活動地點',
    address VARCHAR(255) NOT NULL comment '活動地址',
    description TEXT  comment '活動簡介'  // TEXT：文本型，通常用來儲存較長的文字資料
    event_status ENUM('準備中', '開賣中', '已結束') DEFAULT '準備中' comment '活動狀態',
    event_image LONGBLOB NOT NULL
);
 */

@Data
public class Events {
	private Integer eventId;
	private String eventName;
	private String eventDate;
	private String venue;
	private String description;
	
	private String sellDate;
	private String address;
	private InputStream eventImage;
	private String eventStatus;
}

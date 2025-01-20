package ticket.model.dto;

import lombok.Data;

@Data
public class OrderDto {
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
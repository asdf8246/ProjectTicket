package com.example.demo.ticket.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
	private Integer eventId;
	private String eventName;
	private Date eventDate;
	private String venue;
	private String description;
	
	private Date sellDate;
	private String address;
	private byte[] eventImage;
	private String eventStatus;
	
	private String eventDateStr;
    private String sellDateStr;
	
	
	public EventDto(String eventName, Date eventDate, String venue, String description, Date sellDate,
			String address, byte[] eventImage) {
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.venue = venue;
		this.description = description;
		this.sellDate = sellDate;
		this.address = address;
		this.eventImage = eventImage;
	}
	
	
	
}

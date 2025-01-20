package ticket.model.dto;

import java.io.InputStream;

import lombok.Data;

@Data
public class EventDto {
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

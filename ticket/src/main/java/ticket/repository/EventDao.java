package ticket.repository;

import java.io.InputStream;
import java.util.List;

import ticket.model.entity.Events;

public interface EventDao {
	
	// 多筆:查詢所有
	List<Events> findAllEvents();
	
	// 查詢該筆
	Events getEvent(Integer eventId);
	
	// 新增
	Integer addEvent(Events events);
	
	// 修改
	void updateEventName(Integer eventId, String eventName);
	
	void updateEventDate(Integer eventId, String eventDate);
	
	void updateSellDate(Integer eventId, String sellDate);
	
	void updateVenue(Integer eventId, String venue);
	
	void updateAddress(Integer eventId, String address);
	
	void updateDescription(Integer eventId, String description);
	
	void updateEventImage(Integer eventId, InputStream eventImage);
	
	// 刪除
	void deleteEvent(Integer eventId);
	
	// 搜尋
	List<Events> getSearchEvents(String search);
	
}

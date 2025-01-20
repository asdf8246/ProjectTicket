package com.example.demo.ticket.service;

import java.util.Date;
import java.util.List;
import com.example.demo.ticket.model.dto.EventDto;

public interface EventService {
	// 所有
		public List<EventDto> findAllEvents();
		
		//新增
		public Integer appendEvent(EventDto eventDto);
		
		// 刪除
		public void deleteEvent(String eventId);
		
		// 取得指定
		public EventDto getEvent(String eventId);
		
		//修改
		public void updateEvent(String eventId, String eventName, Date eventDate, Date sellDate, String venue, String address, String description, byte[] eventImage);
		
		// 搜尋
		public List<EventDto> getSearchEvents(String search);
}

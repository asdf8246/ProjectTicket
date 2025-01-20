package com.example.demo.ticket.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ticket.model.dto.EventDto;
import com.example.demo.ticket.model.entity.Events;
import com.example.demo.ticket.repository.EventRepository;
import com.example.demo.ticket.service.EventService;


@Service
public class EventServiceImpl implements EventService{
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	// 所有
	@Override
	public List<EventDto> findAllEvents() {
		
		return eventRepository.findAllEvents().stream()
						.map(Events -> modelMapper.map(Events, EventDto.class))
						.collect(Collectors.toList());
	}
	
	//新增
	@Override
	public Integer appendEvent(EventDto eventDto) {
		
		Events event = modelMapper.map(eventDto, Events.class);
		
        // 當前的時間
        String now = LocalDateTime.now().format(dtf);
        
        LocalDateTime nowTime = LocalDateTime.parse(now, dtf);
        LocalDateTime sellDateTime = eventDto.getSellDate().toInstant()
											                .atZone(ZoneId.systemDefault())
											                .toLocalDateTime();;

        
        if (sellDateTime.isAfter(nowTime)) {
			event.setEventStatus("準備中");
		} else {
			event.setEventStatus("開賣中");
		}
        System.out.println("儲存前的活動日期是: " + event.getEventDate());
        Events savedEvent = eventRepository.save(event);
        
		Integer eventId  = savedEvent.getEventId();
		return eventId;
	}
	
	// 刪除
	@Override
	public void deleteEvent(String eventId) {
		eventRepository.deleteById(Long.parseLong(eventId));
	}
	
	// 取得指定
	@Override
	public EventDto getEvent(String eventId) {
		
		return eventRepository.findById(Long.parseLong(eventId))
						.map(Events -> modelMapper.map(Events, EventDto.class)).orElse(null);
				
	}
	
	//修改
	@Override
	public void updateEvent(String eventId, String eventName, Date eventDate, Date sellDate, String venue, String address, String description, byte[] eventImage) {
		if (!eventName.isEmpty()) {
			eventRepository.updateEventName(Integer.parseInt(eventId), eventName);
		}
		if (eventDate != null) {
			eventRepository.updateEventDate(Integer.parseInt(eventId), eventDate);
		}
		if (sellDate != null) {
			eventRepository.updateSellDate(Integer.parseInt(eventId), sellDate);
		}
		if (!venue.isEmpty()) {
			eventRepository.updateVenue(Integer.parseInt(eventId), venue);
		}
		if (!address.isEmpty()) {
			eventRepository.updateAddress(Integer.parseInt(eventId), address);
		}
		if (!description.isEmpty()) {
			eventRepository.updateDescription(Integer.parseInt(eventId), description);
		}
		if (eventImage != null) {
			eventRepository.updateEventImage(Integer.parseInt(eventId), eventImage);
		}
	}
	
	// 搜尋
	@Override
	public List<EventDto> getSearchEvents(String search){

		return eventRepository.getSearchEvents(search).stream()
						.map(Events -> modelMapper.map(Events, EventDto.class))
						.collect(Collectors.toList());
		
	}
}

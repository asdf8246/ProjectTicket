package com.example.demo.ticket.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ticket.model.dto.EventDto;
import com.example.demo.ticket.service.impl.EventServiceImpl;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/home")
@Controller
public class HomeController {
	
	@Autowired
	private EventServiceImpl eventService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@GetMapping
	public String getHomePage(HttpSession session, Model model) {
		
		List<EventDto> homeEventDtos = eventService.findAllEvents().stream()
				.peek(eventDto -> {
                    // 格式化 eventDate 和 sellDate，並設定到原來的 EventDto 中
                    if (eventDto.getEventDate() != null) {
                        eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
                    }
                    if (eventDto.getSellDate() != null) {
                        eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
                    }
                })
		        .filter(eventDto -> !eventDto.getEventStatus().equals("已結束"))
		        .map(eventDto -> modelMapper.map(eventDto, EventDto.class))  // 使用 ModelMapper 進行映射
		        .collect(Collectors.toList());

		    // 取得所有事件ID，並打亂順序
		    List<Integer> eventIds = homeEventDtos.stream()
		        .map(EventDto::getEventId)
		        .collect(Collectors.toList());
		    
		    Collections.shuffle(eventIds, new Random());
		    
		    
		    Integer mainEventId = null;
		    List<Integer> randomEventIds = null;
		    // 選擇三個隨機事件ID
		    if (eventIds.isEmpty()) {
		    	System.out.println("Array is empty.");
			}else {
				mainEventId = eventIds.get(0);
			    randomEventIds = eventIds.subList(1, Math.min(3, eventIds.size()));
			}
		    
		
	    model.addAttribute("mainEventId", mainEventId);
	    model.addAttribute("randomEventIds", randomEventIds);
	    model.addAttribute("homeEventDtos", homeEventDtos);
		

		return "home";
	}
	
	@GetMapping("/search")
	public String getSearchPage(@RequestParam String search, HttpSession session, Model model) {
		
		List<EventDto> searchEventDtos = eventService.getSearchEvents(search).stream()
												.peek(eventDto -> {
								                    // 格式化 eventDate 和 sellDate，並設定到原來的 EventDto 中
								                    if (eventDto.getEventDate() != null) {
								                        eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
								                    }
								                    if (eventDto.getSellDate() != null) {
								                        eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
								                    }
								                })
								                .collect(Collectors.toList());
		
		List<EventDto> homeEventDtos = eventService.findAllEvents().stream()
		        .filter(eventDto -> !eventDto.getEventStatus().equals("已結束"))
		        .map(eventDto -> modelMapper.map(eventDto, EventDto.class))  // 使用 ModelMapper 進行映射
		        .collect(Collectors.toList());

		    // 取得所有事件ID，並打亂順序
		    List<Integer> eventIds = homeEventDtos.stream()
		        .map(EventDto::getEventId)
		        .collect(Collectors.toList());
		    
		    Collections.shuffle(eventIds, new Random());

		    // 選擇三個隨機事件ID
		    Integer mainEventId = eventIds.get(0);
		    List<Integer> randomEventIds = eventIds.subList(1, Math.min(3, eventIds.size()));

		model.addAttribute("mainEventId", mainEventId);
		model.addAttribute("randomEventIds", randomEventIds);
		
		model.addAttribute("searchEventDtos", searchEventDtos);
		

		return "home_search";
	}
	
	
	@PostMapping
	public String postSearch(@RequestParam(required = false) String search, HttpSession session, Model model) throws UnsupportedEncodingException {
		if (search != null && !search.trim().isEmpty()) {
            // URL 編碼以防止特殊字符問題
            String encodedSearch = URLEncoder.encode(search, "UTF-8");
			return "redirect: /home/search?search=" + encodedSearch;
        }
		
		return "forward:/home";
	}
}

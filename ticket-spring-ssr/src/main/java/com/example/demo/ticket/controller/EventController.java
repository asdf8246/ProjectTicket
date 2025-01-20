package com.example.demo.ticket.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.ticket.aop.CheckUserRole;
import com.example.demo.ticket.model.dto.EventDto;
import com.example.demo.ticket.model.dto.SeatCategoriesDto;
import com.example.demo.ticket.service.impl.EventServiceImpl;
import com.example.demo.ticket.service.impl.SeatCategoriesServiceImpl;

import jakarta.servlet.http.HttpSession;
/**
 查詢所有: GET /events
 查詢單筆: GET /event/get?eventId=1
 新增單筆: POST /event/add
 修改單筆: POST /event/update?eventId=1
 刪除單筆: GET /event/delete?eventId=1 
 */


@Controller
@RequestMapping("/event")
public class EventController {
	
	@Autowired
	private EventServiceImpl eventService;
	
	@Autowired
	private SeatCategoriesServiceImpl seatCategoriesService;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	@CheckUserRole
	@GetMapping
	public String getAllEvents(HttpSession session, Model model) {
		List<EventDto> eventDtos = eventService.findAllEvents().stream()
							                .peek(eventDto -> {
							                    if (eventDto.getEventDate() != null) {
							                        eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
							                    }
							                    if (eventDto.getSellDate() != null) {
							                        eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
							                    }
							                })
							                .collect(Collectors.toList());
		model.addAttribute("eventDtos", eventDtos);
		return "events";
	}
	
	@CheckUserRole
	@GetMapping("/add")
	public String getAddEvent(HttpSession session, Model model) {
		return "event_add";
	}
	
	@CheckUserRole
	@GetMapping("/get")
	public String getEventUpdate(@RequestParam String eventId, HttpSession session, Model model) {
		EventDto eventDto = eventService.getEvent(eventId);
		if (eventDto.getEventDate() != null) {
            eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
        }
        if (eventDto.getSellDate() != null) {
            eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
        }
		List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategories(eventId);
		model.addAttribute("eventDto", eventDto);
		model.addAttribute("seatCategoriesDto", seatCategoriesDto);
		return "event_update";
	}
	
	@CheckUserRole
	@GetMapping("/delete")
	public String deleteEvent(@RequestParam String eventId, HttpSession session, Model model) {
		eventService.deleteEvent(eventId);
		return "redirect:/event";
	}
	
	@GetMapping("/view")
	public String getEventPage(@RequestParam String eventId, HttpSession session, Model model) {
		EventDto eventDto = eventService.getEvent(eventId);
		if (eventDto.getEventDate() != null) {
            eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
        }
        if (eventDto.getSellDate() != null) {
            eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
        }
		List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategories(eventId);
		model.addAttribute("eventDto", eventDto);
		model.addAttribute("seatCategoriesDto", seatCategoriesDto);
		return "event_view";
	}
	
	@CheckUserRole
	@GetMapping("/chart")
	public String getEventChart(@RequestParam String eventId, HttpSession session, Model model) {
		EventDto eventDto = eventService.getEvent(eventId);
		if (eventDto.getEventDate() != null) {
            eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
        }
        if (eventDto.getSellDate() != null) {
            eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
        }
		List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategoriesChart(eventId);
		model.addAttribute("seatCategoriesDto", seatCategoriesDto);
		model.addAttribute("eventDto", eventDto);
		return "event_chart";
	}
	
	@CheckUserRole
	@PostMapping("/add")
	public String postAddEvent(@RequestParam String eventName,
					           @RequestParam String eventDate,
					           @RequestParam String sellDate,
					           @RequestParam String venue,
					           @RequestParam String county,
					           @RequestParam String district,
					           @RequestParam String address,
					           @RequestParam String description,
					           @RequestParam MultipartFile file,  // 這裡使用 MultipartFile 處理檔案
					           @RequestParam(value = "categoryName") String[] categoryNames,
					           @RequestParam(value = "seatPrice") String[] seatPrices,
					           @RequestParam(value = "numSeats") String[] numSeatss,
					           RedirectAttributes redirectAttributes) throws ParseException {
		
		// 檢查檔案是否有上傳
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "檔案上傳失敗，未選擇檔案！");
            return "forward:/event/add";  // 重定向到事件頁面並顯示錯誤訊息
        }
        
        String fullAddress = county + district + address;

        Date dateEventDate = sdf.parse(eventDate+":00");
        
        Date dateSellDate = sdf.parse(sellDate+":00");
        
        try {
            // 取得檔案的 InputStream
        	byte[] eventImage = file.getBytes();
            
            EventDto eventDto = new EventDto(eventName, dateEventDate, venue, description, dateSellDate, fullAddress, eventImage);
            
            // 呼叫 service 層保存事件並取得 eventId
            Integer seatEventId = eventService.appendEvent(eventDto);
            
            // 呼叫 seatCategoriesService 保存座位類別
            seatCategoriesService.appendSeatCategory(seatEventId, categoryNames, seatPrices, numSeatss);
            
            // 重定向至事件頁面
            return "redirect:/event";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新增活動失敗：" + e.getMessage());
            return "redirect:/event/add";  // 失敗後重定向並顯示錯誤訊息
        }
		
	}
	
	@CheckUserRole
	@PostMapping("/update")
	public String postUpdateEvent(@RequestParam String eventId,
								  @RequestParam String eventName,
						          @RequestParam String eventDate,
						          @RequestParam String sellDate,
						          @RequestParam String venue,
						          @RequestParam String county,
						          @RequestParam String district,
						          @RequestParam String address,
						          @RequestParam String description,
						          @RequestParam MultipartFile file,  // 這裡使用 MultipartFile 處理檔案
						          @RequestParam(value = "seatCategoryId") String[] seatCategoryIds,
						          @RequestParam(value = "categoryName") String[] categoryNames,
						          @RequestParam(value = "seatPrice") String[] seatPrices,
						          @RequestParam(value = "numSeats") String[] numSeatss,
						          RedirectAttributes redirectAttributes) {
		
		byte[] eventImage = null;
		String fullAddress = county + district + address;
		
		try {
			
			if (file.isEmpty()) {
	            eventImage = file.getBytes();
	        }
			
			Date dateEventDate = sdf.parse(eventDate+":00");
	        
	        Date dateSellDate = sdf.parse(sellDate+":00");
            
			eventService.updateEvent(eventId, eventName, dateEventDate, dateSellDate, venue, fullAddress, description, eventImage);
			seatCategoriesService.updateSeatCategory(eventId, seatCategoryIds, categoryNames, seatPrices, numSeatss);
			return "redirect:/event";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新活動失敗：" + e.getMessage());
            return "redirect:/event/update";  // 失敗後重定向並顯示錯誤訊息
        }
		
	}
	
}

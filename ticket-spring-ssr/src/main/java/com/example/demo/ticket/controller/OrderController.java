package com.example.demo.ticket.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.ticket.config.RabbitConfig;
import com.example.demo.ticket.model.dto.EventDto;
import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.model.dto.OrderMessage;
import com.example.demo.ticket.model.dto.SeatCategoriesDto;
import com.example.demo.ticket.model.dto.UserCert;
import com.example.demo.ticket.service.impl.EventServiceImpl;
import com.example.demo.ticket.service.impl.OrderServiceImpl;
import com.example.demo.ticket.service.impl.SeatCategoriesServiceImpl;
import com.example.demo.ticket.service.impl.SeatsServiceImpl;
import com.example.demo.ticket.util.CheckUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@RequestMapping("/order")
@Controller
public class OrderController{
	
	@Autowired
	private OrderServiceImpl orderService;
	
	@Autowired
	private EventServiceImpl eventService;
	
	@Autowired
	private SeatCategoriesServiceImpl seatCategoriesService;
	
	@Autowired
	private SeatsServiceImpl seatsService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private CheckUser checkUser;
	
	@GetMapping("/buy")
	public String getBuyOrder(@RequestParam String eventId, HttpSession session,
            Model model, RedirectAttributes redirectAttributes) {
		
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
		OrderDto orderDto = orderService.checkUserOrderStatus(userId, eventId);
		
		if (orderDto != null) {
            if ("pending".equals(orderDto.getOrderStatus())) {
                redirectAttributes.addFlashAttribute("error", "尚有未付款訂單!");
                return "redirect:/user/order"; // 如果是待支付订单，跳转并显示消息
            }

            if ("paid".equals(orderDto.getOrderStatus())) {
                redirectAttributes.addFlashAttribute("error", "已完成票券購買!");
                return "redirect:/user/order"; // 如果是已支付订单，跳转并显示消息
            }
        }
		
		String sellDate = sdf.format(eventService.getEvent(eventId).getSellDate());
		// 定義時間格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 將資料庫時間字符串轉換為 LocalDateTime
        LocalDateTime sellDateTime = LocalDateTime.parse(sellDate, formatter);
        // 獲取當前時間
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        if (sellDateTime.isAfter(currentDateTime)) {
        	model.addAttribute("message", "執行錯誤操作!!!");
			return "error";
		}
        
        List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategories(eventId);
		EventDto eventDto = eventService.getEvent(eventId);
		
		if (eventDto.getEventDate() != null) {
            eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
        }
        if (eventDto.getSellDate() != null) {
            eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
        }
		
		model.addAttribute("userId", userId.toString());
		model.addAttribute("eventDto", eventDto);
		model.addAttribute("seatCategoriesDto", seatCategoriesDto);
		return "order_buy";
		
	}
	
	@GetMapping("/pay")
	public String getPayOrder(@RequestParam String orderId, HttpSession session,
            Model model, RedirectAttributes redirectAttributes) {
		
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
		
		OrderDto orderDto = orderService.getOrder(orderId);
		List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
		
		if (orderDto == null) {
			redirectAttributes.addFlashAttribute("error", "訂單已取消!");
            return "redirect:/user/order"; // 如果是待支付订单，跳转并显示消息
		}
		
		if (!checkUser.checkOrderUser(orderId, userId)) {
			model.addAttribute("message", "執行錯誤操作!!!");
			return "error";
		}
		
		String eventId = orderDto.getEventId().toString();
		EventDto eventDto = eventService.getEvent(eventId);
		
		if (eventDto.getEventDate() != null) {
            eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
        }
        if (eventDto.getSellDate() != null) {
            eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
        }
		if (orderDto.getOrderDate() != null) {
			orderDto.setOrderDateStr(sdf.format(orderDto.getOrderDate()));
		}
		
		model.addAttribute("eventDto", eventDto);
		model.addAttribute("orderDto", orderDto);
		model.addAttribute("orderSeatsDto", orderSeatsDto);
		return "order_pay";
	}
	
	@GetMapping("/delete")
	public String getDeleteOrder(@RequestParam String orderId, @RequestParam(required = false) String eventId, HttpSession session,
            Model model) {
		
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
		String userRole = userCert.getRole();
		
		if (!checkUser.checkOrderUser(orderId, userId) && !CheckUser.checkUserRole(userId, userRole)) {
			model.addAttribute("message", "執行錯誤操作!!!");
			return "error";
		}
		
		
		
		List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
		String seatStatus = "available";
		if (eventId == null) {
			Integer orderEventId = orderService.getOrder(orderId).getEventId();
			seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, orderEventId.toString());
			
		} else {
			seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, eventId);
		}
		
		orderService.deleteOrder(orderId);
		
		if (eventId == null) {
			return "redirect:/user/order";
		}

		return "redirect:/event/view?eventId=" + eventId;
		
	}
	
	@GetMapping("/finish")
	public String getFinishOrder(@RequestParam String orderId, HttpSession session,
            Model model) {
		
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
			
		if (!checkUser.checkOrderUser(orderId, userId)) {
			model.addAttribute("message", "執行錯誤操作!!!");
			return "error";
		}
		
		OrderDto orderDto = orderService.getOrder(orderId);
		String eventId = orderDto.getEventId().toString();
		
		List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
		String seatStatus = "sold";
		seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, eventId);
		String orderStatus = "paid";
		orderService.updateOrderStatus(orderId, orderStatus);
		return "redirect:/user/order";	
		
	}
	
	@GetMapping("/cancel")
	public String getCancelOrder(@RequestParam String orderId, HttpSession session,
            Model model) {
			
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
		String userRole = userCert.getRole();
		
		if (!checkUser.checkOrderUser(orderId, userId) && !CheckUser.checkUserRole(userId, userRole)) {
			model.addAttribute("message", "執行錯誤操作!!!");
			return "error";
		}
		
		OrderDto orderDto = orderService.getOrder(orderId);
		String eventId = orderDto.getEventId().toString();
		
		List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
		String seatStatus = "available";
		seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, eventId);
		String orderStatus = "canceled";
		orderService.updateOrderStatus(orderId, orderStatus);
		return "redirect:/user/order";
	}
	
	@PostMapping("/buy")
    public String postBuyOrder(HttpSession session,
						       @RequestParam String[] seatCategoryIds,
						       @RequestParam String[] seatPrices,
						       @RequestParam String[] numSeatss,
						       @RequestParam String eventId,
						       @RequestParam String eventName, 
						       RedirectAttributes redirectAttributes,
						       HttpServletResponse resp) throws IOException {
        
        // 取得 session 中的用戶認證信息
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        Integer userId = userCert.getUserId();

        // 创建订单信息
        OrderMessage orderMessage = new OrderMessage(userId.toString(), eventId, eventName, seatPrices, numSeatss, seatCategoryIds);
        System.out.println(eventId);
        System.out.println(orderMessage);
        // 將訂單消息轉為 JSON 字符串
        //String orderMessageJson = new Gson().toJson(orderMessage);
        
        // 發送消息到 RabbitMQ
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, orderMessage);
        System.out.println("-----------------------------------------------執行了postBuyOrder-------------------------------------------");

        
        // 设置响应内容类型为 JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        redirectAttributes.addFlashAttribute("message", "訂單已成功提交，處理中...");
        // 返回響應
        return "redirect:/home";
    }
	
}

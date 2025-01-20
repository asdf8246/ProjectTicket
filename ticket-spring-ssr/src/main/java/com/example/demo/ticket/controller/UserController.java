package com.example.demo.ticket.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ticket.aop.CheckUserRole;
import com.example.demo.ticket.exception.UserInsufficientPermissionsException;
import com.example.demo.ticket.exception.UserNotFoundException;
import com.example.demo.ticket.model.dto.EventDto;
import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.model.dto.UserCert;
import com.example.demo.ticket.model.dto.UserDto;
import com.example.demo.ticket.service.impl.EventServiceImpl;
import com.example.demo.ticket.service.impl.OrderServiceImpl;
import com.example.demo.ticket.service.impl.UserServiceImpl;
import com.example.demo.ticket.util.CheckUser;

import jakarta.servlet.http.HttpSession;

/**
 request  +-------------+         +-------------+        +---------+
--------> | UserServlet | ------> | UserServise | -----> | UserDao | -----> MySQL(web.users)
          | (Controller)| <-----  |             | <----- |         | <-----
          +-------------+ UserDto +-------------+  User  +---------+
          		 |   	   (Dto)				 (Entity)
          		 |
          		 v
          +-------------+
<-------- |   user.jsp  |
 response |    (View)   |
          +-------------+
          
 查詢所有: GET /user
 查詢單筆: GET /user/get?username=admin
 新增單筆: POST /user/add
 修改單筆: POST /user/update?userId=1
 刪除單筆: GET /user/delete?userId=1 
 修改密碼: GET /user/update/password (得到修改密碼頁面)
 修改密碼: POST /user/update/password (修改密碼處理程序)
          
 */

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private OrderServiceImpl orderService;
	
	@Autowired
	private EventServiceImpl eventService;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@CheckUserRole
	@GetMapping
	public String getUserList(HttpSession session, Model model) {
		List<UserDto> userDtos = userService.findAll();
		model.addAttribute("userDtos", userDtos);
		return "user";
	}
	
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam String userId, 
            HttpSession session, Model model) {
		
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer certUserId = userCert.getUserId();
		String certUserRole = userCert.getRole();
		
		if (!CheckUser.checkUserId(userId, certUserId) && !CheckUser.checkUserRole(certUserId,certUserRole)) {
			model.addAttribute("message", "執行錯誤操作!!!");
			return "error";
		}
		
		userService.deleteUser(userId);
		
		if (CheckUser.checkUserRole(certUserId, certUserRole)) {
			return "redirect:/user";
		}
		
		// 刪除完畢後，重新執行首頁
		// 將 session 失效
		session.invalidate();
		return "redirect:/home";
	}
	
	@GetMapping("/get")
    public String getUser(@RequestParam String userPhonenumber, 
                          Model model) throws UserNotFoundException {
        UserDto userDto = userService.getUserByPhonenumber(userPhonenumber);
        model.addAttribute("userDto", userDto);
        return "user_update";
    }
	
	@GetMapping("/update/password")
    public String updatePasswordPage() {
        return "update_password";
    }
	
	
	@GetMapping("/order")
    public String getUserOrders(HttpSession session, Model model) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        List<OrderDto> orderDtos = orderService.getUserOrders(userCert.getUserId()).stream()
        								.peek(orderDto -> {
        									if (orderDto.getOrderDate() != null) {
        										orderDto.setOrderDateStr(sdf.format(orderDto.getOrderDate()));
        									}
        								}).collect(Collectors.toList());
        model.addAttribute("orderDtos", orderDtos);
        return "user_order";
    }
	
	@GetMapping("/order/view")
    public String viewOrder(@RequestParam String orderId, Model model) {
        List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
        OrderDto orderDto = orderService.getOrder(orderId);

        Integer eventId = orderDto.getEventId();
        EventDto eventDto = eventService.getEvent(eventId.toString());
        
        if (eventDto.getEventDate() != null) {
            eventDto.setEventDateStr(sdf.format(eventDto.getEventDate()));
        }
        if (eventDto.getSellDate() != null) {
            eventDto.setSellDateStr(sdf.format(eventDto.getSellDate()));
        }
		if (orderDto.getOrderDate() != null) {
			orderDto.setOrderDateStr(sdf.format(orderDto.getOrderDate()));
		}
        

        model.addAttribute("orderSeatsDto", orderSeatsDto);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("eventDto", eventDto);
        return "user_order_view";
    }
    

    @GetMapping("/view")
    public String viewUser(HttpSession session, Model model) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        UserDto userDto = userService.getUser(userCert.getUserId());
        model.addAttribute("userDto", userDto);
        return "user_view";
    }
    
    @GetMapping("/update")
    public String getUserUpdate(HttpSession session, Model model) {
    	UserCert userCert = (UserCert)session.getAttribute("userCert");
    	UserDto userDto = userService.getUser(userCert.getUserId());
		model.addAttribute("userDto", userDto);
		return "user_update";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam String userId, @RequestParam String username,
                             @RequestParam String phonenumber, @RequestParam String email,
                             @RequestParam String role) {
        userService.updateUser(userId, username, phonenumber, email, role);
        return "redirect:/user";
    }

    @PostMapping("/update/password")
    public String updatePassword(HttpSession session, @RequestParam String oldPassword,
                                 @RequestParam String newPassword, Model model) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");

        try {
            userService.updatePassword(userCert.getUserId(), userCert.getPhonenumber(), oldPassword, newPassword);
            model.addAttribute("message", "密碼更新成功");
            return "result";
        } catch (Exception e) {
            model.addAttribute("error", "密碼錯誤!");
            return "update_password";
        }
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String username, @RequestParam String phonenumber,
                          @RequestParam String password, @RequestParam String email,
                          @RequestParam String role) {
    	
    	UserDto userDto = new UserDto(username, phonenumber, password, email, role);
    	
        userService.appendUser(userDto);
        return "redirect:/user";
    }
	
	
	@ExceptionHandler(UserInsufficientPermissionsException.class)
	public String handleUserInsufficientPermissionsException(UserInsufficientPermissionsException e, Model model) {
		model.addAttribute("message", e.getMessage());
		return "error";
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFoundException(UserNotFoundException e, Model model) {
		return "redirect:/home";
	}
	
}

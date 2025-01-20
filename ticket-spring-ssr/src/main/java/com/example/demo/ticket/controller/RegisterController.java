package com.example.demo.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ticket.model.dto.UserDto;
import com.example.demo.ticket.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/register")
@Controller
public class RegisterController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping
	public String getRegisterPage(HttpSession session, Model model) {
		return "register";
	}
	
	@PostMapping
	public String postRrgister(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam String phonenumber, HttpSession session, Model model) {
		String userRole = "ROLE_USER";
		UserDto userDto = new UserDto(username, phonenumber, password, email, userRole);
		
		try {
			userService.appendUser(userDto);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("error", e.getMessage());
			return "register";
		}
		
		model.addAttribute("message", "註冊成功!");
		return "result";
		
	}
	
}

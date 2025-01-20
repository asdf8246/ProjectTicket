package com.example.demo.ticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;



@RequestMapping("/logout")
@Controller
public class LogoutController {
	
	@GetMapping
	public String logout(HttpSession session, Model model) {
		
		// 判斷是否登入
		if (session.getAttribute("userCert")==null) {
			return "redirect:/home";
		}
		
		// 將 session 失效
		session.invalidate(); //所有 session 失效，sessionId 會重發
		//session.setAttribute("userCert", null); // 只讓憑證的 session變數 失效，但 sessionId 不會重發
				
		model.addAttribute("message", "登出成功");
		return "result";
		
	}

}

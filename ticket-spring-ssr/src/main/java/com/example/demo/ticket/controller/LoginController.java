package com.example.demo.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ticket.exception.CertException;
import com.example.demo.ticket.model.dto.UserCert;
import com.example.demo.ticket.service.impl.CertServiceImpl;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/login")
public class LoginController{
	
	@Autowired
	private CertServiceImpl certService;
	
	@GetMapping
	public String getLoginPage(HttpSession session, Model model) {
		
		// 判斷是否登入
		if (session.getAttribute("userCert")!=null) {
			return "redirect:/home";
		}
		
		return "login";
	}
	
	@PostMapping
	public String postLogin(@RequestParam String account, @RequestParam String password, @RequestParam String captcha, HttpSession session, Model model) {
		
		//驗證帳密取得憑證
		UserCert userCert = null;
		
		String captchaInSession = (String)session.getAttribute("captcha");
		
		if (captcha == null || !captcha.equalsIgnoreCase(captchaInSession)) {
        	// CAPTCHA 驗證失敗，提示錯誤
            model.addAttribute("error", "驗證碼錯誤!");
            return "login";
		}
		
		try {
			userCert = certService.gerCert(account, password);
		} catch (CertException e) {
			model.addAttribute("error", e.getMessage());
			return "login";
		}
		
		session.setAttribute("userCert", userCert);// 放憑證

		model.addAttribute("message", "登入成功");
		
		if (session.getAttribute("redirectURL")==null) {
			return "redirect:/home";
		} else {
			String redirectURL = session.getAttribute("redirectURL").toString();
			session.setAttribute("redirectURL", null);
			return "redirect:" + redirectURL;
		}
	}
	
}

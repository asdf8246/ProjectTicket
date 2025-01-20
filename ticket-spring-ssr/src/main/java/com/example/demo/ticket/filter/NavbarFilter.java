package com.example.demo.ticket.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.ticket.model.dto.UserCert;
import com.example.demo.ticket.service.impl.UserServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NavbarFilter extends OncePerRequestFilter{
	
	private UserServiceImpl userService;  // 用來接收注入的 UserServiceImpl

    // 提供 setter 方法讓 Spring 可以注入 userService
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer login = (session.getAttribute("userCert") != null) ? 1 : 0;
		// 判斷是否登入
		if (login == 1) {
			String userName = userService.getUser(userCert.getUserId()).getUsername();
			request.setAttribute("userName", userName);
			String certUserRole = userCert.getRole();
			request.setAttribute("userRole", certUserRole);
		}
		request.setAttribute("login", login);
				
		filterChain.doFilter(request, response);
	}
}

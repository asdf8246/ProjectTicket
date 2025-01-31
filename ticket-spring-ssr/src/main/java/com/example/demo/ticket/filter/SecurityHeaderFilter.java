package com.example.demo.ticket.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class SecurityHeaderFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// HSTS Header
		response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
				
		// X-Frame-Options 防止點擊劫持攻擊
		response.setHeader("X-Frame-Options", "DENY");
				
		// X-Content-Type-Options 防止 MIME 類型攻擊
		response.setHeader("X-Content-Type-Options", "nosniff");
				
		// X-XSS-Protection 啟用 XSS 防護(防止外部惡意的 script 程式攻擊)
		response.setHeader("X-XSS-Protection", "1; mode=block");
				
		// 設定 Cookie 的 SameSite 屬性
		// response.setHeader("Set-Cookie", "myKey=1234abcd; SameSite=strict; Secure; HttpOnly");
				
		// 自行加入其他安全標頭 ...
				
		filterChain.doFilter(request, response);
	}
	
}

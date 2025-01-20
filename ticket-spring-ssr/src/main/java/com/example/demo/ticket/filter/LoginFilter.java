package com.example.demo.ticket.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 設定字符編碼
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=utf-8");

        // 判斷是否有憑證
        HttpSession session = request.getSession(false);  // 不会创建新的 Session
        if (session != null && session.getAttribute("userCert") == null) {
            // 如果没有登录，保存当前请求 URL
            String fullUrl = request.getRequestURL().toString();
            String queryString = request.getQueryString();
            if (queryString != null) {
                fullUrl += "?" + queryString;
            }
            session.setAttribute("redirectURL", fullUrl);

            // 重定向到登录页面
            response.sendRedirect("/ticket/login");
            return;  // 停止继续执行过滤链
        }

        // 如果已登录，继续执行请求链
        filterChain.doFilter(request, response);
    }
    
}

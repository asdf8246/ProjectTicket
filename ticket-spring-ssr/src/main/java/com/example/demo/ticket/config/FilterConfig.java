package com.example.demo.ticket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.ticket.filter.LoginFilter;
import com.example.demo.ticket.filter.NavbarFilter;
import com.example.demo.ticket.filter.SecurityHeaderFilter;
import com.example.demo.ticket.service.impl.UserServiceImpl;

@Configuration
public class FilterConfig {
	
	@Autowired
    private UserServiceImpl userService;

    @Bean
    FilterRegistrationBean<LoginFilter> loginFilter() {
        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginFilter());
        registrationBean.addUrlPatterns("/user/*", "/order/*");  // 配置过滤器的 URL 路径
        return registrationBean;
    }

    @Bean
    FilterRegistrationBean<SecurityHeaderFilter> securityHeaderFilter() {
        FilterRegistrationBean<SecurityHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SecurityHeaderFilter());
        registrationBean.addUrlPatterns("/*");  // 配置该过滤器作用于所有路径
        registrationBean.setOrder(0);
        return registrationBean;
    }
    
    @Bean
    FilterRegistrationBean<NavbarFilter> navbarFilter() {
    	NavbarFilter filter = new NavbarFilter();
    	filter.setUserService(userService);  // 手動注入 userService
        FilterRegistrationBean<NavbarFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");  // 配置该过滤器作用于所有路径
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
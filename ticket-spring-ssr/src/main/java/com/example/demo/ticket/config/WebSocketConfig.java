package com.example.demo.ticket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo.ticket.socket.OrderDataSocketHandler;
import com.example.demo.ticket.socket.SeatDataSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
	private SeatDataSocketHandler seatDataSocketHandler;
	
	@Autowired
	private OrderDataSocketHandler orderDataSocketHandler;
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(seatDataSocketHandler, "/seatDataSocket")
                .setAllowedOrigins("*");
        registry.addHandler(orderDataSocketHandler, "/orderDataSocket")
                .setAllowedOrigins("*");
    }

}
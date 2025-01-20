package com.example.demo.ticket.controller;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.ticket.service.impl.EventServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@RequestMapping("/image")
@Controller
public class ImageController {
	
	@Autowired
	EventServiceImpl eventService;
	
	@GetMapping
	public void getImage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String eventId = req.getParameter("id");
		
		if (eventId != null) {
			byte[] eventImage = eventService.getEvent(eventId).getEventImage();
			
			// 確保圖片流存在
	        if (eventImage == null) {
	            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "圖片未找到");
	            return;
	        }
			
	        resp.setContentType("application/octet-stream"); 
			
			// 确保在响应未提交的情况下写入图片
			if (!resp.isCommitted()) {
			    try (OutputStream out = resp.getOutputStream()) {
			    	out.write(eventImage);  // 直接將 byte[] 寫入回應
		            out.flush();
			    } catch (IOException e) {
			        // 捕获获取输出流时的IO异常
			        System.out.println("获取输出流时发生错误: " + e.getMessage());
			    }
			} else {
			    System.out.println("响应已经提交，无法继续写入图片数据");
			}
		}else {
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少 eventId 參數");
	    }
	}
	
}

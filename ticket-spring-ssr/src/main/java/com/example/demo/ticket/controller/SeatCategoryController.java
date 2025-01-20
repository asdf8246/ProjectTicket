package com.example.demo.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ticket.service.impl.SeatCategoriesServiceImpl;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/SeatCategory")
public class SeatCategoryController {
	
	@Autowired
	private SeatCategoriesServiceImpl seatCategoriesService;
	
	@GetMapping("/delete")
	public String deleteSeatCategories(@RequestParam String eventId, @RequestParam String seatCategoryId, HttpSession session, Model model) {
		
		seatCategoriesService.deleteSeatCategorie(seatCategoryId);
		
		return "redirect:/event/get?eventId=" + eventId;
	}
	
	
}

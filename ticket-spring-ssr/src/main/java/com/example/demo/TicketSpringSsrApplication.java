package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
@EnableJpaRepositories(basePackages = "com.example.demo.ticket.repository")
public class TicketSpringSsrApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketSpringSsrApplication.class, args);
	}

}

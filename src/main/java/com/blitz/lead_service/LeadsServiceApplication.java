package com.blitz.lead_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LeadsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadsServiceApplication.class, args);
	}

}
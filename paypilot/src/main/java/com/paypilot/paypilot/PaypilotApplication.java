package com.paypilot.paypilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaypilotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypilotApplication.class, args);
		
		System.out.println("==================================");
		System.out.println("Application started successfully");
		System.out.println("==================================");

	}

}

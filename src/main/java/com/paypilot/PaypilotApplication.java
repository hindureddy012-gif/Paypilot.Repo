package com.paypilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaypilotApplication {

    public static void main(String[] args) {

        SpringApplication.run(PaypilotApplication.class, args);
    }
}
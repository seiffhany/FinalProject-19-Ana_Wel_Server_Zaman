package com.example.answer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnswerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnswerServiceApplication.class, args);
	}

}

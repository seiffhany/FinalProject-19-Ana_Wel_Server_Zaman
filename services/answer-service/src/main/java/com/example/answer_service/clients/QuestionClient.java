package com.example.answer_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "question-service", url = "http://localhost:8081/question")
public interface QuestionClient {
//    @GetMapping("{id}")
//    public User getUserByID(@PatchMapping UUID id);
}

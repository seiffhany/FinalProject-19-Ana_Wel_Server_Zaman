package com.example.answer_service.clients;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.answer_service.dto.QuestionDTO;

@FeignClient(name = "question-service", url = "${question.service.url}")
public interface QuestionClient {
    @GetMapping("/questions/{id}")
    public QuestionDTO getQuestionById(@PathVariable("id") UUID id);
}

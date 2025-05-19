package com.example.user_service.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.user_service.dto.QuestionDTO;

@FeignClient(name = "question-service", url = "${question.service.url}")
public interface QuestionServiceClient {

    @GetMapping("/questions/author/{authorId}")
    List<QuestionDTO> getUserQuestions(@PathVariable("authorId") UUID authorId);
}

package com.example.user_service.clients;

import com.example.user_service.dto.AnswerDTO;
import com.example.user_service.dto.QuestionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "question-service", url = "${question.service.url}")
public interface QuestionServiceClient {

    @GetMapping("/questions/author/{authorId}")
    List<QuestionDTO> getUserQuestions(@PathVariable("authorId") UUID authorId);
}

package com.example.user_service.clients;

import com.example.user_service.dto.AnswerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "answer-service", url = "${answer.service.url}")
public interface AnswerServiceClient {

    @GetMapping("/answer/getAllAnswersFromUser/{userId}")
    List<AnswerDTO> getUserAnswers(@PathVariable("userId") UUID userId);
}

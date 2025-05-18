package com.example.user_service.service;

import com.example.user_service.client.AnswerServiceClient;
import com.example.user_service.client.QuestionServiceClient;
import com.example.user_service.dto.AnswerDTO;
import com.example.user_service.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final QuestionServiceClient questionServiceClient;
    private final AnswerServiceClient answerServiceClient;

    public List<QuestionDTO> getUserQuestions(UUID userId) {
        return questionServiceClient.getUserQuestions(userId);
    }

    public List<AnswerDTO> getUserAnswers(UUID userId) {
        return answerServiceClient.getUserAnswers(userId);
    }
}
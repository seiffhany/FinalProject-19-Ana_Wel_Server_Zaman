package com.example.answer_service.service;

import com.example.answer_service.repository.AnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
}

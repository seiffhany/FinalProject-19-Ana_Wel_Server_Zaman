package com.example.answer_service.service;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer addAnswer(@RequestBody Answer answer) {
        return this.answerRepository.save(answer);
    }
}

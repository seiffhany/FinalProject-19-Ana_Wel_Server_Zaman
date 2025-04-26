package com.example.answer_service.controller;

import com.example.answer_service.model.Answer;
import com.example.answer_service.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/addAnswer")
    public Answer addAnswer(@RequestBody Answer answer) {
        return this.answerService.addAnswer(answer);
    }
}

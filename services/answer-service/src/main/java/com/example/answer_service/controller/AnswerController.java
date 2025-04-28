package com.example.answer_service.controller;

import com.example.answer_service.dto.UpdateAnswerRequest;
import com.example.answer_service.model.Answer;
import com.example.answer_service.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PutMapping("/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable("id") UUID answerId, @RequestBody UpdateAnswerRequest request)
    {
        Answer updatedAnswer = answerService.updateAnswer(answerId, request);
        return ResponseEntity.ok(updatedAnswer);
    }


}

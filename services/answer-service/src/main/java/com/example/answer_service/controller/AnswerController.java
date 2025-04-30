package com.example.answer_service.controller;

import com.example.answer_service.dto.UpdateAnswerRequest;
import com.example.answer_service.model.Answer;
import com.example.answer_service.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable UUID answerId, @RequestBody String content) {
        try {
            Answer updatedAnswer = answerService.updateAnswer(answerId, content);
            return ResponseEntity.ok(updatedAnswer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update answer: " + e.getMessage());
        }
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswer(@PathVariable UUID answerId) {
        try {
            answerService.deleteAnswer(answerId);
            return ResponseEntity.ok("Answer and its children deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete answer: " + e.getMessage());
        }
    }
  
    @PostMapping("/replyToAnswer")
    public Answer replyToAnswer(@RequestBody Answer answer){
        return this.answerService.replyToAnswer(answer);
    }
}

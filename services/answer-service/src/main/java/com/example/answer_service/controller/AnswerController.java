package com.example.answer_service.controller;

import com.example.answer_service.dto.UpdateAnswerRequest;
import com.example.answer_service.model.Answer;
import com.example.answer_service.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @GetMapping("/question/{questionID}")
    public ResponseEntity<List<Answer>> getAllAnswer(@PathVariable UUID questionID) {
        List<Answer> answers = answerService.getAllAnswerByQuestionId(questionID);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<?> getAnswer(@PathVariable UUID answerId) {
        Answer answer = answerService.getAnswerById(answerId);
        return ResponseEntity.ok(answer);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllAnswerfromUser(@PathVariable UUID userId) {
        List<Answer> answers = answerService.getAllAnswerByUserId(userId);
        return ResponseEntity.ok(answers);

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
    @DeleteMapping("/question/{questionID}")
    public ResponseEntity<String> deleteAllAnswersByQuestionId(@PathVariable UUID questionID) {
        try {
            answerService.deleteAllAnswersByQuestionId(questionID);
            return ResponseEntity.ok("Answers deleted successfully!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete answers from the Question: " + e.getMessage());
        }
    }


    @PostMapping("/replyToAnswer")
    public Answer replyToAnswer(@RequestBody Answer answer){
        return this.answerService.replyToAnswer(answer);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAnswersByUserId(@PathVariable UUID userId) {
        try {
            List<Answer> answers = answerService.getAnswersByUserId(userId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch answers: " + e.getMessage());
        }
    }

    //Add it lama ngeeb el user token
    //Mesh hayenfa3 ykoon fy etnen methods b nafs el Get mapping path 
    @GetMapping("/user/logged-in/{userId}")
    public ResponseEntity<?> getAnswersByLoggedInUser(@PathVariable UUID userId) {
        try {
            List<Answer> answers = answerService.getAnswersByLoggedInUser(userId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch answers: " + e.getMessage());
        }
    }
}

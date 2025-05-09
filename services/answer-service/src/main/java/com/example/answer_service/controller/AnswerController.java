package com.example.answer_service.controller;

import com.example.answer_service.commands.receiver.AnswerReceiver;
import com.example.answer_service.dto.UpdateAnswerRequest;
import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
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
    public Answer addAnswer(@RequestBody Answer answer, @RequestParam UUID loggedInUser) {
        return this.answerService.addAnswer(answer, loggedInUser);
    }

    @PostMapping("/replyToAnswer")
    public Answer replyToAnswer(@RequestBody Answer answer, @RequestParam UUID loggedInUser) {
        return this.answerService.replyToAnswer(answer, loggedInUser);
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<Answer> getAnswer(@PathVariable UUID answerId) {
        Answer answer = answerService.getAnswerById(answerId);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/getAllAnswersByQuestionID/{questionID}")
    public ResponseEntity<List<AnswerService.AnswerWithReplies>> getAllAnswersByQuestionID(@PathVariable UUID questionID) {
        List<AnswerService.AnswerWithReplies> nestedAnswers = answerService.getNestedAnswers(questionID);
        return ResponseEntity.ok(nestedAnswers);
    }

    @GetMapping("/getAllAnswersFromUser/{userId}")
    public ResponseEntity<?> getAllAnswersFromUser(@PathVariable UUID userId) {
        try {
            List<Answer> answers = answerService.getAllAnswerByUserId(userId);
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

    @DeleteMapping("/deleteAllAnswersByQuestionId/{questionID}")
    public ResponseEntity<String> deleteAllAnswersByQuestionId(@PathVariable UUID questionID) {
        try {
            answerService.deleteAllAnswersByQuestionId(questionID);
            return ResponseEntity.ok("Answers deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete answers from the Question: " + e.getMessage());
        }
    }

    @PostMapping("/upvote/{answerId}")
    public ResponseEntity<?> upVoteAnswer(
            @PathVariable UUID answerId,
            @RequestParam UUID userId) {
        try {
            answerService.upVoteAnswer(answerId, userId);
            return ResponseEntity.ok("Answer upvoted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upvote answer: " + e.getMessage());
        }
    }

    @PostMapping("/downvote/{answerId}")
    public ResponseEntity<?> downVoteAnswer(
            @PathVariable UUID answerId,
            @RequestParam UUID userId) {
        try {
            answerService.downVoteAnswer(answerId, userId);
            return ResponseEntity.ok("Answer downvoted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to downvote answer: " + e.getMessage());
        }
    }


    @PutMapping("/markBestAnswer/{answerId}")
    public ResponseEntity<Void> markBestAnswer(@PathVariable UUID answerId, @RequestParam UUID loggedInUser) {
        this.answerService.markBestAnswer(answerId, loggedInUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getFilteredAnswers/{questionId}")
    public List<Answer> getFilteredAnswers(
            @PathVariable UUID questionId,
            @RequestParam(defaultValue = "recency") String filter) {
        return this.answerService.getFilteredAnswers(questionId, filter);
    }
}
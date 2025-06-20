package com.example.answer_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.answer_service.model.Answer;
import com.example.answer_service.service.AnswerService;

@RestController
@RequestMapping("/answers")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/addAnswer")
    public Answer addAnswer(@RequestBody Answer answer, @RequestHeader("userId") UUID loggedInUser) {
        return this.answerService.addAnswer(answer, loggedInUser);
    }

    @PostMapping("/replyToAnswer")
    public Answer replyToAnswer(@RequestBody Answer answer, @RequestHeader("userId") UUID loggedInUser) {
        return this.answerService.replyToAnswer(answer, loggedInUser);
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<Answer> getAnswer(@PathVariable UUID answerId) {
        try {
            Answer answer = answerService.getAnswerById(answerId);
            return ResponseEntity.ok(answer);
        } catch (ResponseStatusException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            throw ex;
        }
    }

    @GetMapping("/getAllAnswersByQuestionID/{questionID}")
    public ResponseEntity<List<AnswerService.AnswerWithReplies>> getAllAnswersByQuestionID(
            @PathVariable UUID questionID) {
        List<AnswerService.AnswerWithReplies> nestedAnswers = answerService.getNestedAnswers(questionID);
        return ResponseEntity.ok(nestedAnswers);
    }

    @GetMapping("/getAllAnswersFromUser/{userId}")
    public ResponseEntity<?> getAllAnswersFromUser(@PathVariable UUID userId) {
        try {
            List<Answer> answers = answerService.getAllAnswerByUserId(userId);
            return ResponseEntity.ok(answers);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Answer or user not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to mark best answer: " + ex.getMessage());
        }
    }

    @GetMapping("/getAllRepliesFromAnswer/{answerId}")
    public ResponseEntity<?> getAllRepliesFromAnswer(@PathVariable UUID answerId) {
        try {
            List<Answer> answers = answerService.getRepliesByAnswerId(answerId);
            return ResponseEntity.ok(answers);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Answer not found: " + ex.getMessage());
        }
    }

    // Add it lama ngeeb el user token
    // Mesh hayenfa3 ykoon fy etnen methods b nafs el Get mapping path
    @GetMapping("/user/logged-in")
    public ResponseEntity<?> getAnswersByLoggedInUser(@RequestHeader("userId") UUID userId) {
        try {
            List<Answer> answers = answerService.getAnswersByLoggedInUser(userId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch answers: " + e.getMessage());
        }
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable UUID answerId, @RequestBody String content,
            @RequestHeader("userId") UUID userId) {
        try {
            Answer updatedAnswer = answerService.updateAnswer(answerId, content, userId);
            return ResponseEntity.ok(updatedAnswer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update answer: " + e.getMessage());
        }
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswer(@PathVariable UUID answerId, @RequestHeader("userId") UUID userId) {
        try {
            answerService.deleteAnswer(answerId, true, userId);
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
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No answers found for the question ID: " + questionID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete answers from the Question: " + e.getMessage());
        }
    }

    @PostMapping("/upvote/{answerId}")
    public ResponseEntity<?> upVoteAnswer(
            @PathVariable UUID answerId,
            @RequestHeader("userId") UUID userId) {
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
            @RequestHeader("userId") UUID userId) {
        try {
            answerService.downVoteAnswer(answerId, userId);
            return ResponseEntity.ok("Answer downvoted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to downvote answer: " + e.getMessage());
        }
    }

    @PutMapping("/markBestAnswer/{answerId}")
    public ResponseEntity<String> markBestAnswer(@PathVariable UUID answerId,
            @RequestHeader("userId") UUID loggedInUser) {
        this.answerService.markBestAnswer(answerId, loggedInUser);

        return ResponseEntity.ok("Marked Message");
    }

    @GetMapping("/getFilteredAnswers/{questionId}")
    public List<Answer> getFilteredAnswers(
            @PathVariable UUID questionId,
            @RequestParam(defaultValue = "recency") String filter) {
        return this.answerService.getFilteredAnswers(questionId, filter);
    }
}
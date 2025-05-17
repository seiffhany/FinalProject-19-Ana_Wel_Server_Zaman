package com.example.question_service.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.question_service.exception.AuthorNotFoundException;
import com.example.question_service.exception.InvalidSortCriterionException;
import com.example.question_service.exception.QuestionNotFoundException;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.question_service.model.Question;
import com.example.question_service.service.QuestionService;

@RestController
@RequestMapping("/questions")
@Validated
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    //error payload DTO
    private record ErrorBody(int status, String message) {}

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return ResponseEntity.ok(questionService.addQuestion(question));
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable UUID id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable UUID id, @RequestBody Question updated) {
        return ResponseEntity.ok(questionService.updateQuestion(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(questionService.getQuestion(id));
        } catch (QuestionNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBySort(
            @RequestParam
            @Pattern(regexp = "date|views|votes", flags = Pattern.Flag.CASE_INSENSITIVE,
                    message = "sortBy must be date, views, or votes")
            String sortBy,
            @RequestParam(required = false) String filter) {

        try {
            return ResponseEntity.ok(questionService.findBySort(sortBy, filter));
        } catch (InvalidSortCriterionException e) {
            return err(HttpStatus.BAD_REQUEST, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<?> upvote(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(questionService.upvote(id));
        } catch (QuestionNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<?> downvote(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(questionService.downvote(id));
        } catch (QuestionNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> byAuthor(@PathVariable String authorId) {
        try {
            List<Question> list = questionService.byAuthor(authorId);
            return ResponseEntity.ok(list);
        } catch (AuthorNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    // helper methods
    private ResponseEntity<ErrorBody> err(HttpStatus s, Exception e) {
        return ResponseEntity.status(s).body(new ErrorBody(s.value(), e.getMessage()));
    }
    private ResponseEntity<ErrorBody> unknown(Exception e) {
        log.error("Unexpected error", e);
        return err(HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException("Internal server error"));
    }
}
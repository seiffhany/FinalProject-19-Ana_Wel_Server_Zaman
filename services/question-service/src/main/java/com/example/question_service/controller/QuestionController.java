package com.example.question_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Question>> findQuestionsBySort(
            @RequestParam String sortBy,
            @RequestParam(required = false) String filter) {
        return null;
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<Question> upvoteQuestion(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<Question> downvoteQuestion(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Question>> getQuestionsByAuthor(@PathVariable Long authorId) {
        return null;
    }
}
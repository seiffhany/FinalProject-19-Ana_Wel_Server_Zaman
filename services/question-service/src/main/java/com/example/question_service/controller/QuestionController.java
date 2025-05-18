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
import com.example.question_service.rabbitmq.RabbitMQProducer;
import com.example.question_service.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final RabbitMQProducer rMqProducer;

    @Autowired
    public QuestionController(QuestionService questionService, RabbitMQProducer rMqProducer) {
        this.questionService = questionService;
        this.rMqProducer = rMqProducer;
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return null;
    }

    @GetMapping("/bypass")
    public ResponseEntity<String> sayHello() {
        rMqProducer.sendUpvoteNotification("seif.naguib@gmail.com", "What is the capital of France?", "John Doe");
        return ResponseEntity.ok("This route successfully bypassed authentication!");
    }

    @GetMapping("/needs-authentication")
    public ResponseEntity<String> hasAuthenticated() {
        // rMqProducer.sendMessage("Hello, World");
        return ResponseEntity.ok("This route has been reached after successful authentication!");
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
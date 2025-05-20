package com.example.question_service.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.question_service.exception.AuthorNotFoundException;
import com.example.question_service.exception.InvalidSortCriterionException;
import com.example.question_service.exception.QuestionNotFoundException;
import com.example.question_service.model.Question;
import com.example.question_service.model.builder.QuestionBuilder;
import com.example.question_service.rabbitmq.RabbitMQProducer;
import com.example.question_service.service.QuestionService;

import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/questions")
@Validated
public class QuestionController {

    private final QuestionService questionService;
    private final RabbitMQProducer rMqProducer;

    // error payload DTO
    private record ErrorBody(int status, String message) {
    }

    @Autowired
    public QuestionController(QuestionService questionService, RabbitMQProducer rMqProducer) {
        this.questionService = questionService;
        this.rMqProducer = rMqProducer;
    }

    @GetMapping("/bypass")
    public ResponseEntity<String> sayHello() {
        // rMqProducer.sendUpvoteNotification("seif.naguib@gmail.com", "What is the
        // capital of France?", "John Doe");
        return ResponseEntity.ok("This route successfully bypassed authentication!");
    }

    @GetMapping("/needs-authentication")
    public ResponseEntity<String> hasAuthenticated(@RequestHeader("userId") UUID userId) {
        // rMqProducer.sendMessage("Hello, World");
        return ResponseEntity.ok("This route has been reached after successful authentication!\n User ID: " + userId);
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question,
            @RequestHeader("userId") UUID authorId, @RequestHeader("username") String authorUsername) {
        question.setAuthorId(authorId);
        return ResponseEntity.ok(questionService.addQuestion(question, authorId, authorUsername));
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
    public ResponseEntity<Question> updateQuestion(@PathVariable UUID id, @RequestBody Question updated,
            @RequestHeader("userId") UUID requestOwner) {
        return ResponseEntity.ok(questionService.updateQuestion(id, updated, requestOwner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID id, @RequestHeader("userId") UUID requestOwner,
            @RequestHeader("roles") String role) {
        questionService.deleteQuestion(id, requestOwner, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBySort(
            @RequestParam(required = false) @Pattern(regexp = "date|views|votes", flags = Pattern.Flag.CASE_INSENSITIVE, message = "sortBy must be date, views, or votes") String sortBy,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false, defaultValue = "false") boolean ascending) {

        try {
            return ResponseEntity.ok(questionService.findBySort(sortBy, filter, ascending));
        } catch (InvalidSortCriterionException e) {
            return err(HttpStatus.BAD_REQUEST, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @PostMapping("/upvote/{id}")
    public ResponseEntity<?> upvote(@PathVariable UUID id, @RequestHeader("username") String upvoterUsername) {
        try {
            return ResponseEntity.ok(questionService.upvote(id, upvoterUsername));
        } catch (QuestionNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @PostMapping("/downvote/{id}")
    public ResponseEntity<?> downvote(@PathVariable UUID id, @RequestHeader("username") String downvoterUsername) {
        try {
            return ResponseEntity.ok(questionService.downvote(id, downvoterUsername));
        } catch (QuestionNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> byAuthor(@PathVariable UUID authorId) {
        try {
            List<Question> list = questionService.byAuthor(authorId);
            return ResponseEntity.ok(list);
        } catch (AuthorNotFoundException e) {
            return err(HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            return unknown(e);
        }
    }

    @PostMapping("/seed")
    public ResponseEntity<List<Question>> seedDatabase() {
        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new QuestionBuilder()
                .title("How to implement JWT authentication in Spring Boot?")
                .body("I'm building a Spring Boot application and need to implement JWT authentication. What are the best practices and steps to follow?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .tags(Arrays.asList("java", "spring-boot", "jwt", "security"))
                .build();
        questions.add(questionService.addQuestion(q1));

        // Question 2
        Question q2 = new QuestionBuilder()
                .title("Best practices for microservices communication")
                .body("What are the recommended patterns for service-to-service communication in a microservices architecture?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
                .tags(Arrays.asList("microservices", "architecture", "distributed-systems"))
                .build();
        questions.add(questionService.addQuestion(q2));

        // Question 3
        Question q3 = new QuestionBuilder()
                .title("Docker vs Kubernetes: When to use what?")
                .body("I'm confused about when to use Docker and when to use Kubernetes. Can someone explain the key differences and use cases?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"))
                .tags(Arrays.asList("docker", "kubernetes", "containerization", "devops"))
                .build();
        questions.add(questionService.addQuestion(q3));

        // Question 4
        Question q4 = new QuestionBuilder()
                .title("Implementing CQRS pattern in Spring Boot")
                .body("I want to implement CQRS in my Spring Boot application. What are the key components and how should I structure my code?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .tags(Arrays.asList("java", "spring-boot", "cqrs", "design-patterns"))
                .build();
        questions.add(questionService.addQuestion(q4));

        // Question 5
        Question q5 = new QuestionBuilder()
                .title("Best practices for API versioning")
                .body("What are the different approaches to version REST APIs and what are their pros and cons?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"))
                .tags(Arrays.asList("api-design", "rest", "versioning"))
                .build();
        questions.add(questionService.addQuestion(q5));

        // Question 6
        Question q6 = new QuestionBuilder()
                .title("Handling distributed transactions in microservices")
                .body("How do you handle transactions that span multiple microservices? What patterns are available?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
                .tags(Arrays.asList("microservices", "transactions", "distributed-systems"))
                .build();
        questions.add(questionService.addQuestion(q6));

        // Question 7
        Question q7 = new QuestionBuilder()
                .title("Implementing rate limiting in Spring Boot")
                .body("What's the best way to implement rate limiting in a Spring Boot application? Are there any good libraries?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"))
                .tags(Arrays.asList("java", "spring-boot", "rate-limiting", "security"))
                .build();
        questions.add(questionService.addQuestion(q7));

        // Question 8
        Question q8 = new QuestionBuilder()
                .title("Best practices for logging in microservices")
                .body("How should I structure logging in a microservices architecture? What tools and patterns are recommended?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"))
                .tags(Arrays.asList("microservices", "logging", "monitoring"))
                .build();
        questions.add(questionService.addQuestion(q8));

        // Question 9
        Question q9 = new QuestionBuilder()
                .title("Implementing event sourcing with Spring Boot")
                .body("I want to implement event sourcing in my Spring Boot application. What are the key components and considerations?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .tags(Arrays.asList("java", "spring-boot", "event-sourcing", "ddd"))
                .build();
        questions.add(questionService.addQuestion(q9));

        // Question 10
        Question q10 = new QuestionBuilder()
                .title("Best practices for API documentation")
                .body("What tools and approaches do you recommend for documenting REST APIs? How to keep documentation in sync with code?")
                .author(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
                .tags(Arrays.asList("api-documentation", "swagger", "openapi"))
                .build();
        questions.add(questionService.addQuestion(q10));

        return ResponseEntity.ok(questions);
    }

    // helper methods
    private ResponseEntity<ErrorBody> err(HttpStatus s, Exception e) {
        return ResponseEntity.status(s).body(new ErrorBody(s.value(), e.getMessage()));
    }

    private ResponseEntity<ErrorBody> unknown(Exception e) {
        return err(HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException("Internal server error"));
    }
}
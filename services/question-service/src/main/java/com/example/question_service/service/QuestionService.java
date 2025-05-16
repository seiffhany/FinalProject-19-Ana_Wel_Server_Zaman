package com.example.question_service.service;

import com.example.question_service.model.Question;
import com.example.question_service.model.builder.QuestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.question_service.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    // CREATE
    public Question addQuestion(Question questionInput) {
        Question question = new QuestionBuilder()
                .title(questionInput.getTitle())
                .body(questionInput.getBody())
                .author(questionInput.getAuthorId())
                .tags(questionInput.getTags())
                .build();
        return questionRepository.save(question);
    }

    // READ ALL
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // READ SINGLE + increment view count
    public Optional<Question> getQuestionById(UUID id) {
        questionRepository.findById(id).ifPresent(q -> {
            q.setViewCount(q.getViewCount() + 1);
            questionRepository.save(q);
        });
        return questionRepository.findById(id);
    }

    // UPDATE
    public Question updateQuestion(UUID id, Question newData) {
        return questionRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(newData.getTitle());
                    existing.setBody(newData.getBody());
                    existing.setTags(newData.getTags());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return questionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // DELETE
    public void deleteQuestion(UUID id) {
        questionRepository.deleteById(id);
    }
}
package com.example.question_service.model.builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.question_service.model.Question;

public class QuestionBuilder {
    private Question question;

    public QuestionBuilder() {
        question = new Question();
        question.setViewCount(0);
        question.setVoteCount(0);
        question.setCreatedAt(LocalDateTime.now());
    }

    public QuestionBuilder title(String title) {
        question.setTitle(title);
        return this;
    }

    public QuestionBuilder body(String body) {
        question.setBody(body);
        return this;
    }

    public QuestionBuilder author(UUID author) {
        question.setAuthorId(author);
        return this;
    }

    public QuestionBuilder tags(List<String> tags) {
        question.setTags(tags);
        return this;
    }

    public Question build() {
        return question;
    }

}

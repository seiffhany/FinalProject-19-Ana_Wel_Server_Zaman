package com.example.question_service.exception;

public class QuestionNotFoundException extends QuestionServiceException {
    public QuestionNotFoundException(String id) { super("Question " + id + " not found"); }
}

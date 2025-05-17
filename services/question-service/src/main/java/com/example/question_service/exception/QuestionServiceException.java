package com.example.question_service.exception;

public abstract class QuestionServiceException extends RuntimeException {
    protected QuestionServiceException(String message) { super(message); }
}
package com.example.question_service.exception;

public class AuthorNotFoundException extends QuestionServiceException {
    public AuthorNotFoundException(String authorId) { super("Author " + authorId + " does not exist"); }
}
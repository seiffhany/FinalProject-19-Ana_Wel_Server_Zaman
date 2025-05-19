package com.example.question_service.exception;

import java.util.UUID;

public class AuthorNotFoundException extends QuestionServiceException {
    public AuthorNotFoundException(UUID authorId) { super("Author " + authorId + " does not exist"); }
}
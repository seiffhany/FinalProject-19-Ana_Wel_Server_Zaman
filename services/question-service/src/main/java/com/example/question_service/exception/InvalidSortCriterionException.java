package com.example.question_service.exception;


public class InvalidSortCriterionException extends QuestionServiceException {
    public InvalidSortCriterionException(String sort) {
        super("Invalid sort criterion: " + sort + " (allowed: date | views | votes)");
    }
}
package com.example.question_service.service.filter;

import com.example.question_service.model.Question;

import java.util.List;

public interface QuestionFilterStrategy {
    List<Question> filter(List<Question> questions, String value);


}

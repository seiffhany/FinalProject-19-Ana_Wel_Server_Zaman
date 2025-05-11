package com.example.question_service.service.sort;

import com.example.question_service.model.Question;

import java.util.List;

public interface SortStrategy {
    List<Question> sort(List<Question> questions, boolean ascending);
}


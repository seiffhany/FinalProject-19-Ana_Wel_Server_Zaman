package com.example.question_service.service.filter;

import com.example.question_service.model.Question;

import java.util.List;
import java.util.stream.Collectors;

public class TitleFilterStrategy implements QuestionFilterStrategy {

    @Override
    public List<Question> filter(List<Question> questions, String keyword) {
        return questions.stream()
                .filter(q -> q.getTitle() != null && q.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}

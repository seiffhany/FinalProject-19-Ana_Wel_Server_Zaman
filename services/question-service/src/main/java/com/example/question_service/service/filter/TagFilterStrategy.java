package com.example.question_service.service.filter;

import com.example.question_service.model.Question;

import java.util.List;
import java.util.stream.Collectors;


public class TagFilterStrategy implements QuestionFilterStrategy{

    @Override
    public List<Question> filter(List<Question> questions, String tag) {
        return questions.stream()
                .filter(q -> q.getTags() != null && q.getTags().contains(tag))
                .collect(Collectors.toList());
    }


}

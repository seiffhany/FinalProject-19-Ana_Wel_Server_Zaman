package com.example.question_service.service.filter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.question_service.model.Question;

public class TagFilterStrategy implements QuestionFilterStrategy {

    @Override
    public List<Question> filter(List<Question> questions, String tags) {
        // Split the tags string by comma and trim each tag
        List<String> tagList = Arrays.stream(tags.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        return questions.stream()
                .filter(q -> q.getTags() != null &&
                        tagList.stream().allMatch(tag -> q.getTags().contains(tag)))
                .collect(Collectors.toList());
    }
}

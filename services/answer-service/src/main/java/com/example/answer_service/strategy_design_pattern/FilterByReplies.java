package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;

import java.util.*;
import java.util.stream.Collectors;

public class FilterByReplies implements FilterStrategy {

    private final AnswerRepository answerRepository;

    public FilterByReplies(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<Answer> filter(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Answer, Integer> answerReplyCounts = new HashMap<>();

        for (Answer answer : answers) {
            int replyCount = getNumberOfReplies(answer);
            answerReplyCounts.put(answer, replyCount);
        }

        return answerReplyCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int getNumberOfReplies(Answer answer) {
        return answerRepository.findByParentID(answer.getId()).size();
    }
}

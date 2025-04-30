package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;

import java.util.*;

public class FilterByReplies implements Filter {

    private final AnswerRepository answerRepository;

    public FilterByReplies(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
    @Override
    public Answer[] filter(Answer[] answers) {
        if (answers == null || answers.length == 0) {
            return new Answer[0];
        }

        Map<Answer, Integer> answerReplyCounts = new HashMap<>();

        for (Answer answer : answers) {
            int replyCount = getNumberOfReplies(answer);
            answerReplyCounts.put(answer, replyCount);
        }

        return answerReplyCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .toArray(Answer[]::new);
    }

    private int getNumberOfReplies(Answer answer) {
        return answerRepository.findByParentID(answer.getId()).size();
    }
}

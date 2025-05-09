package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;

import java.util.*;

public class FilterByReplies implements FilterStrategy {

    @Override
    public List<Answer> filter(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            return new ArrayList<>();
        }

        Map<UUID, Integer> replyCountMap = new HashMap<>();
        for (Answer answer : answers) {
            UUID parentId = answer.getParentID();
            if (parentId != null) {
                replyCountMap.put(parentId, replyCountMap.getOrDefault(parentId, 0) + 1);
            }
        }
        return answers.stream()
                .sorted((answer1, answer2) -> {
                    int replies1 = replyCountMap.getOrDefault(answer1.getId(), 0);
                    int replies2 = replyCountMap.getOrDefault(answer2.getId(), 0);
                    return Integer.compare(replies2, replies1);
                })
                .toList();
    }

}

package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

import java.util.*;
import java.util.stream.Collectors;

public class FilterByReplies implements FilterStrategy {

    @Override
    public List<Answer> filter(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            return Collections.emptyList();
        }
        Map<UUID, Long> replyCount = answers.stream()
                .filter(a -> a.getParentID() != null)
                .collect(Collectors.groupingBy(Answer::getParentID, Collectors.counting()));
        return answers.stream()
                .filter(a -> a.getParentID() == null)
                .sorted(Comparator.comparingLong(
                        a -> -replyCount.getOrDefault(a.getId(), 0L)))
                .collect(Collectors.toList());
    }

}

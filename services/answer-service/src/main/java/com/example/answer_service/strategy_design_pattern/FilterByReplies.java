package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

import java.util.*;
import java.util.stream.Collectors;

public class FilterByReplies implements FilterStrategy {

    public List<Answer> filter(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            return Collections.emptyList();
        }
        Map<UUID, List<Answer>> parentToChildren = answers.stream()
                .filter(a -> a.getParentID() != null)
                .collect(Collectors.groupingBy(Answer::getParentID, Collectors.toList()));

        Map<UUID, Long> totalDescendantsMap = new HashMap<>();
        for (Answer answer : answers) {
            if (answer.getParentID() == null) {
                long total = countDescendants(answer.getId(), parentToChildren);
                totalDescendantsMap.put(answer.getId(), total);
            }
        }

        return answers.stream()
                .filter(a -> a.getParentID() == null)
                .sorted(Comparator.comparingLong(
                        a -> -totalDescendantsMap.getOrDefault(a.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private long countDescendants(UUID id, Map<UUID, List<Answer>> parentToChildren) {
        long count = 0;
        List<Answer> children = parentToChildren.getOrDefault(id, Collections.emptyList());
        count += children.size();
        for (Answer child : children) {
            count += countDescendants(child.getId(), parentToChildren);
        }
        return count;
    }

}

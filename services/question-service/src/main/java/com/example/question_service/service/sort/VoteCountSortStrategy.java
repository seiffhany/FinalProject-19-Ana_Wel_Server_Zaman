package com.example.question_service.service.sort;

import com.example.question_service.model.Question;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VoteCountSortStrategy implements SortStrategy{
    @Override
    public List<Question> sort(List<Question> questions, boolean ascending) {
        Comparator<Question> comparator = Comparator.comparing(Question::getVoteCount);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return questions.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

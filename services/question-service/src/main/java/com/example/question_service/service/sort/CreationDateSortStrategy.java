package com.example.question_service.service.sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.question_service.model.Question;

public class CreationDateSortStrategy implements SortStrategy {

    @Override
    public List<Question> sort(List<Question> questions, boolean ascending) {
        Comparator<Question> comparator = Comparator.comparing(Question::getCreatedAt);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return questions.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

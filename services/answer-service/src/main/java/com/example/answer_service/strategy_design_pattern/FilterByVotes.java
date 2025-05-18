package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterByVotes implements FilterStrategy {
    @Override
    public List<Answer> filter(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            return new ArrayList<>();
        }

        answers.sort((a1, a2) -> {
            int firstAnswerVotes = a1.getUpVoteCount() - a1.getDownVoteCount();
            int secondAnswerVotes = a2.getUpVoteCount() - a2.getDownVoteCount();
            return Integer.compare(secondAnswerVotes, firstAnswerVotes); // Descending
        });

        return answers;
    }
}
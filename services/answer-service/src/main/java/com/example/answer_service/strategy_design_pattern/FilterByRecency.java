package com.example.answer_service.strategy_design_pattern;

import java.util.ArrayList;
import java.util.List;

import com.example.answer_service.model.Answer;

public class FilterByRecency implements FilterStrategy {

    @Override
    public List<Answer> filter(List<Answer> answers) {
        if (answers == null || answers.size() == 0) {
            return new ArrayList<Answer>();
        }
        for (int i = 0; i < answers.size(); i++) {
            for (int j = 0; j < answers.size() - 1 - i; j++) {
                if (answers.get(j).getCreatedAt().isBefore(answers.get(j + 1).getCreatedAt())) {
                    Answer temp = answers.get(j);
                    answers.set(j, answers.get(j + 1));
                    answers.set(j + 1, temp);
                }
            }
        }
        return answers;
    }
}

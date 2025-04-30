package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

import java.util.Arrays;

public class FilterByRecency implements Filter {

    @Override
    public Answer[] filter(Answer[] answers) {
        if (answers == null || answers.length == 0) {
            return new Answer[0];
        }
        for (int i = 0; i < answers.length; i++) {
            for (int j = 0; j < answers.length - 1 - i; j++) {
                if (answers[j].getCreatedAt().isBefore(answers[j + 1].getCreatedAt())) {
                    Answer temp = answers[j];
                    answers[j] = answers[j + 1];
                    answers[j + 1] = temp;
                }
            }
        }
        return answers;
    }
}

package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

import java.util.Arrays;

public class FilterByVotes implements Filter {
    @Override
    public Answer[] filter(Answer[] answers) {
        if (answers == null || answers.length == 0) {
            return new Answer[0];
        }
        Arrays.sort(answers, (a1, a2) -> {
            int firstAnswerVotes = a1.getUpVoteCount() - a1.getDownVoteCount();
            int secondAnswerVotes = a2.getUpVoteCount() - a2.getDownVoteCount();
            return Integer.compare(secondAnswerVotes, firstAnswerVotes);
        });
        return answers;
    }
}

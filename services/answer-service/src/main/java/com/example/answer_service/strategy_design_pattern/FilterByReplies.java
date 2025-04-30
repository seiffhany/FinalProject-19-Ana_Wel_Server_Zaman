package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

public class FilterByReplies implements Filter {
    @Override
    public Answer[] filter(Answer[] answers) {
        return new Answer[0];
    }
}

package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

public interface Filter {
    Answer[] filter(Answer[] answers);
}

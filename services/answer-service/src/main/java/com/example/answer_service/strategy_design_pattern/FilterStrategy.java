package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;

import java.util.List;

public interface FilterStrategy {
    List<Answer> filter(List<Answer> answers);
}

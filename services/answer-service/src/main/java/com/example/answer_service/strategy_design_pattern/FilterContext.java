package com.example.answer_service.strategy_design_pattern;

import com.example.answer_service.model.Answer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterContext {
    private FilterStrategy strategy;

    public void setStrategy(FilterStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Answer> filter(List<Answer> answers) {
        return strategy.filter(answers);
    }
}
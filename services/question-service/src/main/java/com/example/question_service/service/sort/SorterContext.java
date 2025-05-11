package com.example.question_service.service.sort;

import com.example.question_service.model.Question;

import java.util.List;

public class SorterContext {
    private SortStrategy strategy;

    public SorterContext(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public SortStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Question> execute(List<Question> questions, boolean ascending) {
        return strategy.sort(questions, ascending);
    }
}

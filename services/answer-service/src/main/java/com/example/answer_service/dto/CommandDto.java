package com.example.answer_service.dto;

import com.example.answer_service.model.Answer;

import java.util.UUID;

public class CommandDto {
    private Answer answer;
    private UUID loggedInUser;

    public CommandDto(Answer answer, UUID loggedInUser) {
        this.answer = answer;
        this.loggedInUser = loggedInUser;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public UUID getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UUID loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}

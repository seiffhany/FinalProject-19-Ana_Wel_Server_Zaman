package com.example.answer_service.commands.command;


import com.example.answer_service.model.Answer;

public interface Command {
    void execute(Answer answer);

    void undo(Answer answer);
}
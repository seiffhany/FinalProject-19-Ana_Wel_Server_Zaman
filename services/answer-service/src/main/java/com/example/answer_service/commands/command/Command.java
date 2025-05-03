package com.example.answer_service.commands.command;


import com.example.answer_service.dto.CommandDto;
import com.example.answer_service.model.Answer;

public interface Command {
    void execute(CommandDto commandDto);

    void undo(CommandDto commandDto);
}
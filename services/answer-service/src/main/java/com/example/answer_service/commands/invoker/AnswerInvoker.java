package com.example.answer_service.commands.invoker;

import com.example.answer_service.commands.command.Command;
import com.example.answer_service.dto.CommandDto;
import com.example.answer_service.model.Answer;

import java.util.UUID;

public class AnswerInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressOption(CommandDto commandDto) {
        command.execute(commandDto);
    }

    public void undoOption(CommandDto commandDto) {
        command.undo(commandDto);
    }
}

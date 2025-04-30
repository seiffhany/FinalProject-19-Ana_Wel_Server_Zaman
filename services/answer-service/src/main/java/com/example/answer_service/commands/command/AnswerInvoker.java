package com.example.answer_service.commands.command;

import java.util.UUID;

public class AnswerInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressOption(UUID answerId) {
        command.execute(answerId);
    }

    public void undoOption(UUID answerId) {
        command.undo(answerId);
    }
}

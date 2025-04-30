package com.example.answer_service.commands.concretecommands;

import com.example.answer_service.commands.command.Command;
import com.example.answer_service.commands.receiver.AnswerReceiver;

import java.util.UUID;

public class MarkBestAnswerCommand implements Command {
    private AnswerReceiver answer;
    public MarkBestAnswerCommand(AnswerReceiver answer) {
        this.answer = answer;
    }
    @Override
    public void execute(UUID answerId) {
        answer.markBestAnswer(answerId);
    }
    @Override
    public void undo(UUID answerId) {
        answer.undoMarkBestAnswer(answerId);
    }
} 
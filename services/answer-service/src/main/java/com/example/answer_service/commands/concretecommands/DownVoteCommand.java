package com.example.answer_service.commands.concretecommands;


import com.example.answer_service.commands.command.Command;
import com.example.answer_service.commands.receiver.AnswerReceiver;

import java.util.UUID;

public class DownVoteCommand implements Command {
    private AnswerReceiver answer;
    public DownVoteCommand(AnswerReceiver answer) {
        this.answer = answer;
    }
    @Override
    public void execute(UUID answerId) {
        answer.DownVote(answerId);

    }
    @Override
    public void undo(UUID answerId) {
        answer.undoDownVote(answerId);
    }
}

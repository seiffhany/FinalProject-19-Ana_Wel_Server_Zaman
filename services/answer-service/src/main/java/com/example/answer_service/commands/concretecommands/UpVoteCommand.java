package com.example.answer_service.commands.concretecommands;


import com.example.answer_service.commands.command.Command;
import com.example.answer_service.commands.receiver.AnswerReceiver;
import com.example.answer_service.model.Answer;

import java.util.UUID;

public class UpVoteCommand implements Command {
    private AnswerReceiver answerReceiver;

    public UpVoteCommand(AnswerReceiver answerReceiver) {
        this.answerReceiver = answerReceiver;
    }

    @Override
    public void execute(Answer answer) {
        answerReceiver.UpVote(answer);

    }

    @Override
    public void undo(Answer answer) {
        answerReceiver.undoUpVote(answer);
    }
}

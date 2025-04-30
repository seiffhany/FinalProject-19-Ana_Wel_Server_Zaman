package com.example.answer_service.commands.receiver;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerReceiver {
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerReceiver(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
    public void UpVote(UUID answerId)
    {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent())
        {
            Answer answer = optionalAnswer.get();
            answer.setUpVoteCount(answer.getUpVoteCount()+1);
            answerRepository.save(answer);
        }
    }
    public void undoUpVote(UUID answerId)
    {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent())
        {
            Answer answer = optionalAnswer.get();
            answer.setUpVoteCount(answer.getUpVoteCount()-1);
            answerRepository.save(answer);
        }
    }
    public void DownVote(UUID answerId)
    {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent())
        {
            Answer answer = optionalAnswer.get();
            answer.setDownVoteCount(answer.getDownVoteCount()+1);
            answerRepository.save(answer);
        }
    }
    public void undoDownVote(UUID answerId)
    {
    }
}

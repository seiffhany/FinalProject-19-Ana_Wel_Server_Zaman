package com.example.answer_service.commands.receiver;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerReceiver {
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerReceiver(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
    @Transactional
    public void UpVote(UUID answerId)
    {
        //user id from the token
        UUID userId = new UUID(0, 0);

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent())
        {
            Answer answer = optionalAnswer.get();

            if(answer.getUpVoters() != null) {
                if (!(answer.getUpVoters().contains(userId))) {
                    answer.addUpVoter(userId);
                    if(answer.getDownVoters() != null) {
                        if ((answer.getDownVoters().contains(userId))) {
                            answer.removeDownVoter(userId);
                        }
                    }
                    answerRepository.save(answer);
                } else {
                    throw new IllegalStateException("User already upVoted this answer");
                }
            }

        }
    }
    @Transactional
    public void undoUpVote(UUID answerId)
    {
        //user id from the token
        UUID userId = new UUID(0, 0);
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent())
        {
            Answer answer = optionalAnswer.get();
            if(answer.getUpVoters() != null) {
                if ((answer.getUpVoters().contains(userId))) {
                    answer.removeUpVoter(userId);
                    answerRepository.save(answer);
                    }
                }
        }
    }
    @Transactional
    public void DownVote(UUID answerId)
    {
        //user id from the token
        UUID userId = new UUID(0, 0);
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent())
        {
            Answer answer = optionalAnswer.get();
            if(answer.getDownVoters() != null) {
                if (!(answer.getDownVoters().contains(userId))) {
                    answer.addDownVoter(userId);
                    if(answer.getUpVoters() != null) {
                        if ((answer.getUpVoters().contains(userId))) {
                            answer.removeUpVoter(userId);
                        }
                    }
                    answerRepository.save(answer);
                } else {
                    throw new IllegalStateException("User already downVoted this answer");
                }
            }
        }
    }
    @Transactional
    public void undoDownVote(UUID answerId)
    {
    }
}

package com.example.answer_service.commands.receiver;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.answer_service.dto.CommandDto;
import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;

@Component
public class AnswerReceiver {
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerReceiver(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Transactional
    public void UpVote(CommandDto commandDto) {
        Answer answer = commandDto.getAnswer();
        UUID userId = commandDto.getLoggedInUser();
        if (answer.getUpVoters() != null) {
            if (!(answer.getUpVoters().contains(userId))) {
                answer.addUpVoter(userId);
                if (answer.getDownVoters() != null) {
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

    public void undoUpVote(CommandDto commandDto) {
        Answer answer = commandDto.getAnswer();
        UUID userId = commandDto.getLoggedInUser();
        if (answer.getUpVoters() != null) {
            if ((answer.getUpVoters().contains(userId))) {
                answer.removeUpVoter(userId);
                answerRepository.save(answer);
            } else {
                throw new IllegalStateException("User didn't upVote this answer");
            }
        }
    }

    @Transactional
    public void DownVote(CommandDto commandDto) {
        Answer answer = commandDto.getAnswer();
        UUID userId = commandDto.getLoggedInUser();
        if (answer.getDownVoters() != null) {
            if (!(answer.getDownVoters().contains(userId))) {
                answer.addDownVoter(userId);
                if (answer.getUpVoters() != null) {
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

    @Transactional
    public void undoDownVote(CommandDto commandDto) {
        Answer answer = commandDto.getAnswer();
        UUID userId = commandDto.getLoggedInUser();
        if (answer.getDownVoters() != null) {
            if ((answer.getDownVoters().contains(userId))) {
                answer.removeDownVoter(userId);
                answerRepository.save(answer);
            } else {
                throw new IllegalStateException("User didn't downVote this answer");
            }
        }
    }

    @Transactional
    public void markBestAnswer(CommandDto commandDto) {
        Answer answer = commandDto.getAnswer();
        UUID userId = commandDto.getLoggedInUser();
        if (!answer.isBestAnswer()) {
            answer.setBestAnswer(true);
            answerRepository.save(answer);
        } else {
            throw new IllegalStateException("CommandDto is already marked as best answer");
        }
    }

    @Transactional
    public void undoMarkBestAnswer(CommandDto commandDto) {
        Answer answer = commandDto.getAnswer();
        if (answer.isBestAnswer()) {
            answer.setBestAnswer(false);
            answerRepository.save(answer);
        } else {
            throw new IllegalStateException("CommandDto is already marked as best answer");
        }
    }
}

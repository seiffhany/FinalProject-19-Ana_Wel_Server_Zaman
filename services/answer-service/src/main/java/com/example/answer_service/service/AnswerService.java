package com.example.answer_service.service;

import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import com.example.answer_service.dto.UpdateAnswerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer addAnswer(@RequestBody Answer answer) {
        return this.answerRepository.save(answer);
    }

    public Answer updateAnswer(UUID answerId, UpdateAnswerRequest request) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setContent(request.getContent());

            if (request.isRemoveImage())
            {
                answer.setImageInByte(null);
            }else if (request.getImage() != null)
            {
                answer.setImageInByte(request.getImage());
            }

            answer.setUpdatedAt(java.time.LocalDateTime.now());
            return answerRepository.save(answer);
        } else {
            throw new RuntimeException("Answer not found with id: " + answerId);
        }
    }
    public void deleteAnswer(UUID answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();

            List<Answer> childAnswers = answerRepository.findByParentID(answer.getId());
            for (Answer child : childAnswers)
            {
                deleteAnswer(child.getId());
            }

            answerRepository.delete(answer);
        }
    }
    }



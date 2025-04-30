package com.example.answer_service.service;

import com.example.answer_service.clients.QuestionClient;
import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import com.example.answer_service.dto.UpdateAnswerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;
    private QuestionClient questionClient;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionClient questionClient) {
        this.answerRepository = answerRepository;
        this.questionClient = questionClient;
    }

    public Answer addAnswer(Answer answer) {
        try {
//            questionClient.getQuestionByID(answer.getQuestionID());
            Answer newAnswer = new Answer(answer.getQuestionID(), answer.getUserId(), answer.getContent());
            return this.answerRepository.save(newAnswer);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist");
        }
    }

    public Answer replyToAnswer(Answer answer) {
        try {
//            questionClient.getQuestionByID(answer.getQuestionID());
            if (answer.getParentID() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad parameters");
            }
            Answer retrievedParentAnswer = this.answerRepository.findAnswerById(answer.getParentID());
            if (retrievedParentAnswer.getId() != null) {
                Answer newAnswer = new Answer(answer.getParentID(), answer.getQuestionID(), answer.getUserId(), answer.getContent());
                return this.answerRepository.save(newAnswer);
            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This parent answer ID doesn't exist");
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist");
        }
    }

//    public Answer updateAnswer(UUID answerId, UpdateAnswerRequest request) {
//        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
//
//        if (optionalAnswer.isPresent()) {
//            Answer answer = optionalAnswer.get();
//            answer.setContent(request.getContent());
//
//            if (request.isRemoveImage())
//            {
//                answer.setImageInByte(null);
//            }else if (request.getImage() != null)
//            {
//                answer.setImageInByte(request.getImage());
//            }
//
//            answer.setUpdatedAt(java.time.LocalDateTime.now());
//            return answerRepository.save(answer);
//        } else {
//            throw new RuntimeException("Answer not found with id: " + answerId);
//        }
//    }
public Answer updateAnswer(UUID answerId,String content) {
    Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
    if (optionalAnswer.isPresent()) {
        Answer answer = optionalAnswer.get();
        answer.setContent(content);
        answer.setUpdatedAt(java.time.LocalDateTime.now());
        return answerRepository.save(answer);
    }
    else {
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



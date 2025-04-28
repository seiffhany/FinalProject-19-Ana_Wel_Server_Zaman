package com.example.answer_service.service;

import com.example.answer_service.clients.QuestionClient;
import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

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
}

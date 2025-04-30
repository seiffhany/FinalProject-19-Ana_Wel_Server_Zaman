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
import java.util.stream.Collectors;

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
    public void markBestAnswer(UUID answerId, UUID currentUserId) {
        Answer targetAnswer = answerRepository.findAnswerById(answerId);

        if (targetAnswer.getParentID() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reply cannot be marked as best answer");
        }

        List<Answer> allAnswers = answerRepository.findByQuestionID(targetAnswer.getQuestionID());

//        UUID questionOwnerId = questionClient.getQuestionOwnerId(targetAnswer.getQuestionID());
//        if (!questionOwnerId.equals(currentUserId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the question author can mark best answer");
//        }

        if (targetAnswer.isBestAnswer()) {
            targetAnswer.setBestAnswer(false);
            answerRepository.save(targetAnswer);
            return;
        }

        for (Answer answer : allAnswers) {
            if (answer.isBestAnswer()) {
                answer.setBestAnswer(false);
                answerRepository.save(answer);
            }
        }
        targetAnswer.setBestAnswer(true);
        answerRepository.save(targetAnswer);
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

    public Answer updateAnswer(UUID answerId, String content) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setContent(content);
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
            for (Answer child : childAnswers) {
                deleteAnswer(child.getId());
            }

            answerRepository.delete(answer);
        }
    }
    public Answer getAnswerById(UUID answerId) {
        try {
            return answerRepository.findAnswerById(answerId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve answer", ex);
        }

    public List<Answer> getAnswersByUserId(UUID userId) {
        return answerRepository.findByUserId(userId);
    }

    //Add it lama ngeeb el user token
    public List<Answer> getAnswersByLoggedInUser(UUID userId) {
        return answerRepository.findByUserId(userId);
    }
}

    public List<Answer> getAllAnswerByUserId(UUID userId) {
        try {
            List<Answer> answers = answerRepository.findByUserId(userId);
            if (answers.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No answers found for user with id: " + userId);
            }
            return answers;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve user's answers", ex);
        }
    }

    public List<Answer> getAllAnswerByQuestionId(UUID questionId) {
        try {
            List<Answer> answers = answerRepository.findByQuestionID(questionId);
            if (answers.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No answers found for question with id: " + questionId);
            }
            return answers;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve answers for question", ex);
        }
    }
    public void deleteAllAnswersByQuestionId(UUID questionId) {
        try {
            List<Answer> answers = answerRepository.findByQuestionID(questionId);
            if (answers.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No answers found for question with id: " + questionId);
            }
            answerRepository.deleteAll(answers);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete answers for question", ex);
        }
    }


}



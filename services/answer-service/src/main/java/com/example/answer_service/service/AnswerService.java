package com.example.answer_service.service;

import com.example.answer_service.clients.QuestionClient;
import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import com.example.answer_service.dto.UpdateAnswerRequest;
import com.example.answer_service.strategy_design_pattern.FilterByRecency;
import com.example.answer_service.strategy_design_pattern.FilterByReplies;
import com.example.answer_service.strategy_design_pattern.FilterByVotes;
import com.example.answer_service.strategy_design_pattern.FilterContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;
    private QuestionClient questionClient;
    private FilterContext filterContext;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionClient questionClient, FilterContext filterContext) {
        this.answerRepository = answerRepository;
        this.questionClient = questionClient;
        this.filterContext = filterContext;
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
            UUID parentID = answer.getParentID();
            Answer retrievedParentAnswer = this.answerRepository.findAnswerById(parentID);
            if (retrievedParentAnswer.getId() != null) {
                Answer newAnswer = new Answer(answer.getParentID(), answer.getQuestionID(), answer.getUserId(), answer.getContent());
                return this.answerRepository.save(newAnswer);
            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This parent answer ID doesn't exist");
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist");
        }
    }

    public List<Answer> getFilteredAnswers(UUID questionID, String filter) {
        try {
//            questionClient.getQuestionByID(answer.getQuestionID());
            List<Answer> answers = this.answerRepository.findByQuestionID(questionID);
            if (answers.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question doesn't have associated answers");
            }

            switch (filter.toLowerCase()) {
                case "recency":
                    filterContext.setStrategy(new FilterByRecency());
                    break;
                case "votes":
                    filterContext.setStrategy(new FilterByVotes());
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter type");
            }

            return this.filterContext.filter(answers);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist or the question doesn't have associated answers");
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
        else {
            throw new RuntimeException("Answer not found with id: " + answerId);
        }
    }

    public Answer getAnswerById(UUID answerId) {
        try {
            return answerRepository.findAnswerById(answerId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve answer", ex);
        }
    }

    //Add it lama ngeeb el user token
    public List<Answer> getAnswersByLoggedInUser(UUID userId) {
        return answerRepository.findByUserId(userId);
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

    public List<Answer> getAllAnswersByQuestionID(UUID questionId) {
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

    public List<AnswerWithReplies> getNestedAnswers(UUID questionId) {
        List<Answer> allAnswers = answerRepository.findByQuestionID(questionId);
        Map<UUID, List<Answer>> repliesMap = allAnswers.stream()
                .filter(a -> a.getParentID() != null)
                .collect(Collectors.groupingBy(Answer::getParentID));

        return allAnswers.stream()
                .filter(a -> a.getParentID() == null)
                .map(parent -> new AnswerWithReplies(parent, repliesMap.getOrDefault(parent.getId(), List.of())))
                .collect(Collectors.toList());
    }

    // DTO for nested response
    public record AnswerWithReplies(Answer answer, List<Answer> replies) {}
}
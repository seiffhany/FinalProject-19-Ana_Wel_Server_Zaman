package com.example.answer_service.service;

import com.example.answer_service.clients.QuestionClient;
import com.example.answer_service.commands.receiver.AnswerReceiver;
import com.example.answer_service.model.Answer;
import com.example.answer_service.repositories.AnswerRepository;
import com.example.answer_service.dto.UpdateAnswerRequest;
import com.example.answer_service.strategy_design_pattern.FilterByRecency;
import com.example.answer_service.strategy_design_pattern.FilterByReplies;
import com.example.answer_service.strategy_design_pattern.FilterByVotes;
import com.example.answer_service.strategy_design_pattern.FilterContext;
import com.example.answer_service.commands.concretecommands.UpVoteCommand;
import com.example.answer_service.commands.concretecommands.DownVoteCommand;
import com.example.answer_service.commands.receiver.AnswerReceiver;
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
    private FilterContext filterContext;
    private AnswerReceiver answerReceiver;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionClient questionClient, FilterContext filterContext, AnswerReceiver answerReceiver) {
        this.answerRepository = answerRepository;
        this.questionClient = questionClient;
        this.filterContext = filterContext;
        this.answerReceiver = answerReceiver;
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
                case "replies":
                    filterContext.setStrategy(new FilterByReplies());
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter type");
            }

            return this.filterContext.filter(answers);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist or the question doesn't have associated answers");
        }
    }

    public void markBestAnswer(UUID answerId) {
        Answer answer = answerRepository.findAnswerById(answerId);
        answerReceiver.markBestAnswer(answer);
    }


    public void upVoteAnswer(UUID answerId, UUID currentUserId)
    {
        //            questionClient.getQuestionByID(answer.getQuestionID());
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
        }

        Answer answer = answerRepository.findAnswerById(answerId);

        if (answer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found with id: " + answerId);
        }

        // Might be changed if user can Upvote  nafso
        // if (answer.getUserId().equals(currentUserId)) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users cannot vote on their own answers");
        // }

        // Check if user has already upvoted
        if (answer.getUpVoters() != null && answer.getUpVoters().contains(currentUserId)) {
            // User has already upvoted, so remove the upvote
            answer.setUserId(currentUserId);
            UpVoteCommand upVoteCommand = new UpVoteCommand(answerReceiver);
            upVoteCommand.undo(answer);
            return;
        }

        // If user has downvoted remove the downvote first
        if (answer.getDownVoters() != null && answer.getDownVoters().contains(currentUserId)) {
            answer.setUserId(currentUserId);
            DownVoteCommand downVoteCommand = new DownVoteCommand(answerReceiver);
            downVoteCommand.undo(answer);
        }
        
        answer.setUserId(currentUserId);
        
        UpVoteCommand upVoteCommand = new UpVoteCommand(answerReceiver);
        try {
            upVoteCommand.execute(answer);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void downVoteAnswer(UUID answerId, UUID currentUserId)
    {
        //            questionClient.getQuestionByID(answer.getQuestionID());
        
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
        }

        Answer answer = answerRepository.findAnswerById(answerId);

        if (answer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found with id: " + answerId);
        }

        // Might be changed if user can downvote nafso
        // if (answer.getUserId().equals(currentUserId)) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users cannot vote on their own answers");
        // }

        // Check if user has already downvoted
        if (answer.getDownVoters() != null && answer.getDownVoters().contains(currentUserId)) {
            // User has already downvoted, so remove the downvote
            answer.setUserId(currentUserId);
            DownVoteCommand downVoteCommand = new DownVoteCommand(answerReceiver);
            downVoteCommand.undo(answer);
            return;
        }

        // If user has upvoted remove the upvote first
        if (answer.getUpVoters() != null && answer.getUpVoters().contains(currentUserId)) {
            answer.setUserId(currentUserId);
            UpVoteCommand upVoteCommand = new UpVoteCommand(answerReceiver);
            upVoteCommand.undo(answer);
        }
        
        answer.setUserId(currentUserId);
        
        DownVoteCommand downVoteCommand = new DownVoteCommand(answerReceiver);
        try {
            downVoteCommand.execute(answer);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
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
    }

    public Answer getAnswerById(UUID answerId) {
        try {
            return answerRepository.findAnswerById(answerId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to retrieve answer", ex);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to retrieve user's answers", ex);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to retrieve answers for question", ex);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete answers for question", ex);
        }
    }
}
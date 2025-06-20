package com.example.answer_service.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.answer_service.clients.QuestionClient;
import com.example.answer_service.commands.command.Command;
import com.example.answer_service.commands.invoker.AnswerInvoker;
import com.example.answer_service.commands.receiver.AnswerReceiver;
import com.example.answer_service.dto.CommandDto;
import com.example.answer_service.dto.DeleteAnswerResponseDTO;
import com.example.answer_service.dto.QuestionDTO;
import com.example.answer_service.model.Answer;
import com.example.answer_service.rabbitmq.RabbitMQProducer;
import com.example.answer_service.repositories.AnswerRepository;
import com.example.answer_service.strategy_design_pattern.FilterByRecency;
import com.example.answer_service.strategy_design_pattern.FilterByReplies;
import com.example.answer_service.strategy_design_pattern.FilterByVotes;
import com.example.answer_service.strategy_design_pattern.FilterContext;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private QuestionClient questionClient;
    private FilterContext filterContext;
    private AnswerInvoker answerInvoker;
    private AnswerReceiver answerReceiver;
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionClient questionClient, FilterContext filterContext,
            AnswerInvoker answerInvoker, AnswerReceiver answerReceiver, RabbitMQProducer rabbitMQProducer) {
        this.answerRepository = answerRepository;
        this.questionClient = questionClient;
        this.filterContext = filterContext;
        this.answerInvoker = answerInvoker;
        this.answerReceiver = answerReceiver;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public static Object createInstance(String className, Class<?>[] paramTypes, Object... args)
            throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor(paramTypes).newInstance(args);
    }

    public Answer addAnswer(Answer answer, UUID loggedInUser) {
        try {
            // questionClient.getQuestionByID(answer.getQuestionID());
            Answer newAnswer = new Answer(answer.getQuestionID(), loggedInUser, answer.getContent());
            Answer saved = this.answerRepository.save(newAnswer);
            sendAnswerToQuestionService(answer.getQuestionID(), 1);
            return saved;
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist");
        }
    }

    public Answer replyToAnswer(Answer answer, UUID loggedInUser) {
        try {
            // questionClient.getQuestionByID(answer.getQuestionID());
            if (answer.getParentID() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad parameters");
            }
            UUID parentID = answer.getParentID();
            Answer retrievedParentAnswer = this.answerRepository.findAnswerById(parentID);
            if (retrievedParentAnswer != null) {
                if (retrievedParentAnswer.getQuestionID().equals(answer.getQuestionID())) {
                    Answer newAnswer = new Answer(answer.getParentID(), answer.getQuestionID(), loggedInUser,
                            answer.getContent());
                    Answer saved = this.answerRepository.save(newAnswer);
                    sendAnswerToQuestionService(answer.getQuestionID(), 1);
                    return saved;
                } else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "This parent answer ID doesn't belong to the same question ID of the input question ID");
            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This parent answer ID doesn't exist");
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question ID doesn't exist");
        }
    }

    public List<Answer> getFilteredAnswers(UUID questionID, String filter) {
        try {
            // questionClient.getQuestionByID(answer.getQuestionID());
            List<Answer> answers = this.answerRepository.findByQuestionID(questionID);
            // if (answers.isEmpty()) {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question doesn't
            // have associated answers");
            // }

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This question ID doesn't exist.");
        }
    }

    public void markBestAnswer(UUID answerId, UUID loggedInUser) {
        Answer answer = answerRepository.findAnswerById(answerId);
        if (answer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found");
        }

        QuestionDTO question = questionClient.getQuestionById(answer.getQuestionID());
        if (!loggedInUser.equals(question.getAuthorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this answer");
        }

        if (answer.getParentID() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reply cannot be marked as best answer");
        }

        try {
            Command markBestAnswerCommand = (Command) createInstance(
                    "com.example.answer_service.commands.concretecommands.MarkBestAnswerCommand",
                    new Class<?>[] { AnswerReceiver.class }, answerReceiver);
            answerInvoker.setCommand(markBestAnswerCommand);

            List<Answer> allAnswers = answerRepository.findByQuestionID(answer.getQuestionID());
            for (Answer a : allAnswers) {
                if (a.isBestAnswer() && !a.getId().equals(answer.getId())) {
                    answerInvoker.undoOption(new CommandDto(a, loggedInUser));
                }
            }

            CommandDto commandDto = new CommandDto(answer, loggedInUser);

            if (answer.isBestAnswer()) {
                answerInvoker.undoOption(commandDto);
                return;
            }
            answerInvoker.pressOption(commandDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void upVoteAnswer(UUID answerId, UUID loggedInUser) {
        // questionClient.getQuestionByID(answer.getQuestionID());

        Answer answer = answerRepository.findAnswerById(answerId);

        if (answer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found with id: " + answerId);
        }

        try {
            Command upVoteCommand = (Command) createInstance(
                    "com.example.answer_service.commands.concretecommands.UpVoteCommand",
                    new Class<?>[] { AnswerReceiver.class }, answerReceiver);
            Command downVoteCommand = (Command) createInstance(
                    "com.example.answer_service.commands.concretecommands.DownVoteCommand",
                    new Class<?>[] { AnswerReceiver.class }, answerReceiver);
            CommandDto commandDto = new CommandDto(answer, loggedInUser);

            // Check if user has already upvoted
            if (answer.getUpVoters() != null && answer.getUpVoters().contains(loggedInUser)) {
                // User has already upvoted, so remove the upvote
                answerInvoker.setCommand(upVoteCommand);
                answerInvoker.undoOption(commandDto);
                return;
            }

            // If user has downvoted remove the downvote first
            if (answer.getDownVoters() != null && answer.getDownVoters().contains(loggedInUser)) {
                answerInvoker.setCommand(downVoteCommand);
                answerInvoker.undoOption(commandDto);
            }

            try {
                answerInvoker.setCommand(upVoteCommand);
                answerInvoker.pressOption(commandDto);
            } catch (IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to process upvote: " + e.getMessage());
        }
    }

    public void downVoteAnswer(UUID answerId, UUID loggedInUser) {
        // questionClient.getQuestionByID(answer.getQuestionID());
        Answer answer = answerRepository.findAnswerById(answerId);

        if (answer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found with id: " + answerId);
        }

        try {
            Command upVoteCommand = (Command) createInstance(
                    "com.example.answer_service.commands.concretecommands.UpVoteCommand",
                    new Class<?>[] { AnswerReceiver.class }, answerReceiver);
            Command downVoteCommand = (Command) createInstance(
                    "com.example.answer_service.commands.concretecommands.DownVoteCommand",
                    new Class<?>[] { AnswerReceiver.class }, answerReceiver);
            CommandDto commandDto = new CommandDto(answer, loggedInUser);

            // Check if user has already downvoted
            if (answer.getDownVoters() != null && answer.getDownVoters().contains(loggedInUser)) {
                // User has already downvoted, so remove the downvote
                answerInvoker.setCommand(downVoteCommand);
                answerInvoker.undoOption(commandDto);
                return;
            }

            // If user has upvoted remove the upvote first
            if (answer.getUpVoters() != null && answer.getUpVoters().contains(loggedInUser)) {
                answerInvoker.setCommand(upVoteCommand);
                answerInvoker.undoOption(commandDto);
            }

            answer.setUserId(loggedInUser);

            try {
                answerInvoker.setCommand(downVoteCommand);
                answerInvoker.pressOption(commandDto);
            } catch (IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to process downvote: " + e.getMessage());
        }
    }

    public Answer updateAnswer(UUID answerId, String content, UUID userId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!answer.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this answer");
            }
            answer.setContent(content);
            answer.setUpdatedAt(java.time.LocalDateTime.now());
            return answerRepository.save(answer);
        } else {
            throw new RuntimeException("Answer not found with id: " + answerId);
        }
    }

    public DeleteAnswerResponseDTO deleteAnswer(UUID answerId, boolean isRoot, UUID userId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (isRoot && !answer.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this answer");
            }
            List<Answer> childAnswers = answerRepository.findByParentID(answer.getId());
            int totalDeleted = 1;

            for (Answer child : childAnswers) {
                DeleteAnswerResponseDTO childResult = deleteAnswer(child.getId(), false, userId);
                totalDeleted += childResult.getTotalAnswersDeleted();
            }

            answerRepository.delete(answer);
            if (isRoot) {
                sendAnswerToQuestionService(answer.getQuestionID(), totalDeleted * -1);
            }
            return new DeleteAnswerResponseDTO(totalDeleted, answerId);
        } else {
            throw new RuntimeException("Answer not found with id: " + answerId);
        }
    }

    public void sendAnswerToQuestionService(UUID questionId, int count) {
        this.rabbitMQProducer.sendAnswerToQuestionService(questionId.toString(), count);
    }

    public Answer getAnswerById(UUID answerId) {
        try {
            return answerRepository.findAnswerById(answerId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", ex);
        }
    }

    // Add it lama ngeeb el user token
    public List<Answer> getAnswersByLoggedInUser(UUID userId) {
        return answerRepository.findByUserId(userId);
    }

    public List<Answer> getAllAnswerByUserId(UUID userId) {
        try {
            List<Answer> answers = answerRepository.findByUserId(userId);
            if (answers == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No  user with this  id: " + userId);
            }
            return answers;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve user's answers",
                    ex);
        }
    }

    public void deleteAllAnswersByQuestionId(UUID questionId) {
        try {
            List<Answer> answers = answerRepository.findByQuestionID(questionId);
            if (answers.isEmpty() || answers == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No  question with this  id: " + questionId);
            }
            int totalDeleted = answers.size();
            answerRepository.deleteAll(answers);
            sendAnswerToQuestionService(questionId, totalDeleted * -1);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No question found with id: " + questionId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete answers for question",
                    ex);
        }
    }

    public List<AnswerWithReplies> getNestedAnswers(UUID questionId) {
        List<Answer> allAnswers = answerRepository.findByQuestionID(questionId);

        // Group answers by their parentID
        Map<UUID, List<Answer>> repliesMap = allAnswers.stream()
                .filter(a -> a.getParentID() != null)
                .collect(Collectors.groupingBy(Answer::getParentID));

        // Recursively build the nested structure
        return allAnswers.stream()
                .filter(a -> a.getParentID() == null) // top-level answers
                .map(parent -> buildAnswerWithReplies(parent, repliesMap))
                .collect(Collectors.toList());
    }

    // Recursive method to build AnswerWithReplies
    private AnswerWithReplies buildAnswerWithReplies(Answer answer, Map<UUID, List<Answer>> repliesMap) {
        List<Answer> directReplies = repliesMap.getOrDefault(answer.getId(), List.of());

        List<AnswerWithReplies> nestedReplies = directReplies.stream()
                .map(reply -> buildAnswerWithReplies(reply, repliesMap))
                .collect(Collectors.toList());

        return new AnswerWithReplies(answer, nestedReplies);
    }

    // Updated DTO to support nested replies
    public record AnswerWithReplies(Answer answer, List<AnswerWithReplies> replies) {
    }

    public List<Answer> getRepliesByAnswerId(UUID answerId) {
        if (!answerRepository.existsById(answerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found");
        }
        List<Answer> allAnswers = answerRepository.findAll();

        Map<UUID, List<Answer>> parentToChildren = allAnswers.stream()
                .filter(a -> a.getParentID() != null)
                .collect(Collectors.groupingBy(Answer::getParentID));

        List<Answer> allReplies = new ArrayList<>();
        collectAllReplies(answerId, parentToChildren, allReplies);
        return allReplies;
    }

    private void collectAllReplies(UUID parentId,
            Map<UUID, List<Answer>> parentToChildren,
            List<Answer> result) {
        List<Answer> directReplies = parentToChildren.getOrDefault(parentId, Collections.emptyList());
        result.addAll(directReplies);
        for (Answer reply : directReplies) {
            collectAllReplies(reply.getId(), parentToChildren, result);
        }
    }

}
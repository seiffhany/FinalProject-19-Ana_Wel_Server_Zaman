package com.example.question_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.question_service.dto.UserDTO;
import com.example.question_service.exception.AuthorNotFoundException;
import com.example.question_service.exception.InvalidSortCriterionException;
import com.example.question_service.exception.QuestionNotFoundException;
import com.example.question_service.model.Question;
import com.example.question_service.model.builder.QuestionBuilder;
import com.example.question_service.rabbitmq.RabbitMQProducer;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.service.client.UserClient;
import com.example.question_service.service.filter.TagFilterStrategy;
import com.example.question_service.service.filter.TitleFilterStrategy;
import com.example.question_service.service.sort.CreationDateSortStrategy;
import com.example.question_service.service.sort.SortStrategy;
import com.example.question_service.service.sort.SorterContext;
import com.example.question_service.service.sort.ViewCountSortStrategy;
import com.example.question_service.service.sort.VoteCountSortStrategy;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserClient userClient;
    private final RabbitMQProducer rabbitMQProducer;

    /* ---------- Sort strategies ---------- */
    private static final Map<String, SortStrategy> SORT_MAP = Map.of(
            "date", new CreationDateSortStrategy(),
            "views", new ViewCountSortStrategy(),
            "votes", new VoteCountSortStrategy());

    /* ---------- Filter strategies -------- */
    private final TagFilterStrategy tagFilter = new TagFilterStrategy();
    private final TitleFilterStrategy titleFilter = new TitleFilterStrategy();

    @Autowired
    public QuestionService(QuestionRepository questionRepository, UserClient userClient,
            RabbitMQProducer rabbitMQProducer) {
        this.questionRepository = questionRepository;
        this.userClient = userClient;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    // CREATE
    public Question addQuestion(Question questionInput, UUID authorId, String authorUsername) {
        Question question = new QuestionBuilder()
                .title(questionInput.getTitle())
                .body(questionInput.getBody())
                .author(questionInput.getAuthorId())
                .tags(questionInput.getTags())
                .build();

        List<UserDTO> followers = userClient.getUserFollowers(authorId);
        String[] recipientUsersEmails = followers.stream()
                .map(UserDTO::getEmail)
                .toArray(String[]::new);

        rabbitMQProducer.sendQuestionCreatedNotification(recipientUsersEmails, question.getTitle(), authorUsername);
        return questionRepository.save(question);
    }

    // CREATE for Seeder
    public Question addQuestion(Question questionInput) {
        Question question = new QuestionBuilder()
                .title(questionInput.getTitle())
                .body(questionInput.getBody())
                .author(questionInput.getAuthorId())
                .tags(questionInput.getTags())
                .build();

        return questionRepository.save(question);
    }

    // READ ALL
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // READ SINGLE + increment view count
    public Optional<Question> getQuestionById(UUID id) {
        questionRepository.findById(id).ifPresent(q -> {
            q.setViewCount(q.getViewCount() + 1);
            questionRepository.save(q);
        });
        return questionRepository.findById(id);
    }

    // UPDATE
    public Question updateQuestion(UUID id, Question newData, UUID requestOwner) {
        Question question = getQuestion(id);
        if (question == null)
            throw new QuestionNotFoundException(id.toString());
        if (!requestOwner.equals(question.getAuthorId()))
            throw new RuntimeException("You are not authorized to update this question");
        question.setTitle(newData.getTitle());
        question.setBody(newData.getBody());
        question.setTags(newData.getTags());
        question.setUpdatedAt(LocalDateTime.now());
        return questionRepository.save(question);
    }

    // DELETE
    public void deleteQuestion(UUID id, UUID requestOwner, String role) {
        Question question = getQuestion(id);
        if (!requestOwner.equals(question.getAuthorId()) && !role.contains("ADMIN"))
            throw new RuntimeException("You are not authorized to delete this question");

        questionRepository.deleteById(id);
        rabbitMQProducer.sendQuestionDeletedToAnswerService(id.toString());
    }

    @Transactional
    public List<Question> findBySort(String sortBy, String filter, boolean ascending) {
        SortStrategy strat = SORT_MAP.get(sortBy.toLowerCase());
        if (strat == null)
            throw new InvalidSortCriterionException(sortBy);

        List<Question> qs = questionRepository.findAll();
        if (filter != null && !filter.isBlank())
            qs = applyFilter(qs, filter);

        // Increment view count for each question
        qs.forEach(q -> {
            q.setViewCount(q.getViewCount() + 1);
            questionRepository.save(q);
        });

        return new SorterContext(strat).execute(qs, ascending);
    }

    @Transactional
    public Question upvote(UUID id, UUID upvoterId, String upvoterUsername) {
        Question q = getQuestion(id);

        // Check if user has already upvoted
        if (q.getUpVoters().contains(upvoterId)) {
            // User has already upvoted, so remove the upvote
            q.removeUpVoter(upvoterId);
        } else {
            // If user has downvoted, remove the downvote first
            if (q.getDownVoters().contains(upvoterId)) {
                q.removeDownVoter(upvoterId);
            }
            q.addUpVoter(upvoterId);
        }

        String authorId = q.getAuthorId().toString();
        UserDTO author = userClient.getUserById(UUID.fromString(authorId));
        if (author != null)
            rabbitMQProducer.sendUpvoteNotification(author.getEmail(), q.getTitle(), upvoterUsername);
        return questionRepository.save(q);
    }

    @Transactional
    public Question downvote(UUID id, UUID downvoterId, String downvoterUsername) {
        Question q = getQuestion(id);

        // Check if user has already downvoted
        if (q.getDownVoters().contains(downvoterId)) {
            // User has already downvoted, so remove the downvote
            q.removeDownVoter(downvoterId);
        } else {
            // If user has upvoted, remove the upvote first
            if (q.getUpVoters().contains(downvoterId)) {
                q.removeUpVoter(downvoterId);
            }
            q.addDownVoter(downvoterId);
        }

        String authorId = q.getAuthorId().toString();
        UserDTO author = userClient.getUserById(UUID.fromString(authorId));
        if (author != null)
            rabbitMQProducer.sendDownvoteNotification(author.getEmail(), q.getTitle(), downvoterUsername);
        return questionRepository.save(q);
    }

    @Transactional
    public List<Question> byAuthor(UUID authorId) {
        if (userClient.getUserById(authorId) == null)
            throw new AuthorNotFoundException(authorId);

        List<Question> questions = questionRepository.findByAuthorId(authorId);

        questions.forEach(q -> {
            q.setViewCount(q.getViewCount() + 1);
            questionRepository.save(q);
        });

        return questions;
    }

    public Question getQuestion(UUID id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id.toString()));
    }

    private List<Question> applyFilter(List<Question> qs, String filter) {
        if (filter.startsWith("tag:"))
            return tagFilter.filter(qs, filter.substring(4));
        if (filter.startsWith("title:"))
            return titleFilter.filter(qs, filter.substring(6));
        return qs;
    }

    @Transactional
    public Question incrementAnswerCount(UUID id, int count) {
        Question question = getQuestion(id);
        question.setAnswerCount(question.getAnswerCount() + count);
        return questionRepository.save(question);
    }
}
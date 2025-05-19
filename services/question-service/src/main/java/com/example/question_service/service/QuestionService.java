package com.example.question_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return questionRepository.findById(id)
                .map(existing -> {
                    if (!existing.getAuthorId().equals(requestOwner))
                        throw new RuntimeException("You are not authorized to update this question");

                    existing.setTitle(newData.getTitle());
                    existing.setBody(newData.getBody());
                    existing.setTags(newData.getTags());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return questionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Question not found"));
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
    public Question upvote(UUID id) {
        Question q = getQuestion(id);
        q.setVoteCount(q.getVoteCount() + 1);
        return questionRepository.save(q);
    }

    @Transactional
    public Question downvote(UUID id) {
        Question q = getQuestion(id);
        q.setVoteCount(q.getVoteCount() - 1);
        return questionRepository.save(q);
    }

    @Transactional
    public List<Question> byAuthor(UUID authorId) {
        if (!userClient.doesUserExist(authorId))
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
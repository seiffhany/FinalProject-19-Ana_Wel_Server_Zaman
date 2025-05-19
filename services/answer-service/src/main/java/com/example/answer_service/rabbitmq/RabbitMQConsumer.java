package com.example.answer_service.rabbitmq;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.answer_service.service.AnswerService;

@Service
public class RabbitMQConsumer {
    private final AnswerService answerService;

    @Autowired
    public RabbitMQConsumer(AnswerService answerService) {
        this.answerService = answerService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUESTION_DELETED_QUEUE_NAME)
    public void handleQuestionDeleted(String questionId) {
        System.out.println("Received question deletion message for question ID: " + questionId);
        try {
            UUID uuid = UUID.fromString(questionId);
            answerService.deleteAllAnswersByQuestionId(uuid);
            System.out.println("Successfully deleted all answers for question ID: " + questionId);
        } catch (Exception e) {
            System.err.println("Error deleting answers for question ID " + questionId + ": " + e.getMessage());
        }
    }
}

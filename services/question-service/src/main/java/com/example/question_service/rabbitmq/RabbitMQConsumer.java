package com.example.question_service.rabbitmq;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.question_service.service.QuestionService;

@Service
public class RabbitMQConsumer {

    @Autowired
    private QuestionService questionService;

    @RabbitListener(queues = RabbitMQConfig.QUESTION_QUEUE_NAME)
    public void receiveNewAnswerIdAndQuestionId(String message) {
        String[] parts = message.split("\\|");
        String questionId = parts[0];
        int count = Integer.parseInt(parts[1]);
        System.out.println("Received question ID: " + questionId);
        System.out.println("Received answer count: " + count);
        questionService.incrementAnswerCount(UUID.fromString(questionId), count);
    }
}

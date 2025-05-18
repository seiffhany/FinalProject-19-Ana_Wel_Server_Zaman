package com.example.question_service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUESTION_QUEUE_NAME)
    public void receiveNewAnswerIdAndQuestionId(String message) {
        String[] parts = message.split("\n");
        System.out.println("Received question ID: " + parts[0]);
        System.out.println("Received answer ID: " + parts[1]);
        // Here you can implement the logic to add the new answer to the question
    }
}

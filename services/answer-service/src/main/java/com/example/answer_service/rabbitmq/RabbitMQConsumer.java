package com.example.answer_service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUESTION_QUEUE_NAME)
    public void receiveDeletedQuestionID(String questionId) {
        System.out.println("Received deleted question ID: " + questionId);
    }
}

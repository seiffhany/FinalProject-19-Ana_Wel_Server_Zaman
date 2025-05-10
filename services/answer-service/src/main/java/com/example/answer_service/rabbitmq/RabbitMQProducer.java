package com.example.answer_service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendAnswerNotification(String question, String answer, String user) {
        String message = user + " answered the question: " + question + " with the answer: " + answer;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
    public void sendAnswerAcceptedNotification(String question) {
        String message = "Your answer to the question: \"" + question + "\" has been accepted.";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
    public void sendAnswerDeletedNotification(String question) {
        String message = "Your answer to the question: \"" + question + "\" has been deleted.";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
    public void sendAnswerToQuestionService(String questionId, String answerId) {
        String message = questionId + "\n" + answerId;
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUESTION_EXCHANGE_NAME, RabbitMQConfig.QUESTION_ROUTING_KEY, message);
    }
}

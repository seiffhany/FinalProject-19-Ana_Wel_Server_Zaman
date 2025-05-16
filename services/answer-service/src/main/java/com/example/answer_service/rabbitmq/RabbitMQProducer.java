package com.example.answer_service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendAnswerNotification(String questionPosterId, String question, String answer, String userNameWhoAnswered) {
        String[] message = {"Answer", questionPosterId, question, answer, userNameWhoAnswered};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
    public void sendAnswerAcceptedNotification(String userId, String question) {
        String[] message = {"Answer Accepted", userId, question};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
    public void sendAnswerDeletedNotification(String userId, String question) {
        String[] message = {"Answer Deleted", userId, question};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
    public void sendAnswerToQuestionService(String questionId, String answerId) {
        String message = questionId + "\n" + answerId;
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUESTION_EXCHANGE_NAME, RabbitMQConfig.QUESTION_ROUTING_KEY, message);
    }
}

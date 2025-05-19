package com.example.answer_service.rabbitmq;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.answer_service.utils.NotificationMessage;
import com.example.answer_service.utils.NotificationMessage.NotificationCategory;
import com.example.answer_service.utils.NotificationMessage.NotificationType;

@Service
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAnswerNotification(UUID questionPosterId, String question, String answer,
            String userNameWhoAnswered) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.ANSWER_NEW)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(questionPosterId.toString()))
                .content(question + "|" + answer + "|" + userNameWhoAnswered)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
        sendAnswerToQuestionService(questionPosterId.toString(), 1);
    }

    public void sendAnswerAcceptedNotification(String userId, String question) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.ANSWER_ACCEPTED)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(userId))
                .content(question)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendAnswerDeletedNotification(String userId, String question) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.ANSWER_DELETED)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(userId))
                .content(question)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendAnswerToQuestionService(String questionId, int count) {
        String message = questionId + "|" + count;
        System.out.println("sendAnswerToQuestionService: " + message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUESTION_EXCHANGE_NAME, RabbitMQConfig.QUESTION_ROUTING_KEY,
                message);
        System.out.println("SENT!");
    }
}

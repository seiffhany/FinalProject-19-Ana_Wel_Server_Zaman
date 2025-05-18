package com.example.answer_service.rabbitmq;

import java.util.Arrays;

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

    public void sendAnswerNotification(String questionPosterId, String question, String answer,
            String userNameWhoAnswered) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.ANSWER_NEW)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(questionPosterId))
                .content(question + "|" + answer + "|" + userNameWhoAnswered)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
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

    public void sendAnswerToQuestionService(String questionId, String answerId) {
        String message = questionId + "\n" + answerId;
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUESTION_EXCHANGE_NAME, RabbitMQConfig.QUESTION_ROUTING_KEY,
                message);
    }
}

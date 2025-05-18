package com.example.question_service.rabbitmq;

import java.util.Arrays;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.question_service.utils.NotificationMessage;
import com.example.question_service.utils.NotificationMessage.NotificationCategory;
import com.example.question_service.utils.NotificationMessage.NotificationType;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUpvoteNotification(String notificationReceiverEmail, String questionContent,
            String upvoterUserName) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.QUESTION_UPVOTE)
                .type(NotificationType.EMAIL_NOTIFICATION)
                .recipientEmails(Arrays.asList(notificationReceiverEmail))
                .content(questionContent + "|" + upvoterUserName)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendDownvoteNotification(String notificationReceiverEmail, String questionContent, String userName) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.QUESTION_DOWNVOTE)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(notificationReceiverEmail))
                .content(questionContent + "|" + userName)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendViewCountNotification(String questionCreatorUserId, String questionContent, int viewCount) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.QUESTION_VIEW_COUNT)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(questionCreatorUserId))
                .content(questionContent + "|" + viewCount)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendQuestionCreatedNotification(String[] recipientUsersIds, String questionContent, String userName) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.QUESTION_NEW)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientEmails(Arrays.asList(recipientUsersIds))
                .content(questionContent + "|" + userName)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendQuestionDeletedToAnswerService(String questionId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUESTION_EXCHANGE_NAME, RabbitMQConfig.ANSWER_ROUTING_KEY,
                questionId);
    }
}
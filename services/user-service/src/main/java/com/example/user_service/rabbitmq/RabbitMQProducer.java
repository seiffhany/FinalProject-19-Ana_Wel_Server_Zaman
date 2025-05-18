package com.example.user_service.rabbitmq;

import java.util.Arrays;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.notification_service.utils.NotificationMessage;
import com.example.shared_models.notification.NotificationMessage.NotificationCategory;
import com.example.shared_models.notification.NotificationMessage.NotificationType;

@Component
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendFollowMessage(String userId, String followerName) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.USER_FOLLOW)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientIds(Arrays.asList(userId))
                .content(followerName)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendLoginNotification(String userId, String device, String location) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.USER_LOGIN)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientIds(Arrays.asList(userId))
                .content(device + "|" + location)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendUserRegistrationNotification(String userId, String email) {
        NotificationMessage message = NotificationMessage.builder()
                .category(NotificationCategory.USER_REGISTRATION)
                .type(NotificationType.IN_APP_NOTIFICATION)
                .recipientIds(Arrays.asList(userId))
                .content(email)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
}
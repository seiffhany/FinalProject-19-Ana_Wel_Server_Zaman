package com.example.user_service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendFollowMessage(String followerName) {
        String message = "User " + followerName + " followed you " ;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
        System.out.println("Follow notification sent: " + message);
    }

    public void sendLoginNotification(String userId, String device, String location) {
        String message = "Login detected for user " + userId + " from " + device + " in " + location;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
        System.out.println("Login notification sent: " + message);
    }

    public void sendUserRegistrationNotification(String email) {
        String message = "Welcome to Ana Wel Server Zaman! Your account with Email: " + email + " has been successfully created.";

        String structuredMessage = "{\"type\":\"welcome\",\"recipientId\":\"" + email + "\",\"content\":\"" + message + "\"}";

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, structuredMessage);
        System.out.println("Welcome notification sent to new user: " + email);
    }
}
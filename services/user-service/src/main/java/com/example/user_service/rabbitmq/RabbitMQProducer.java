package com.example.user_service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendFollowMessage(String userId, String followerName) {
        String[] message = {"Follow", userId, followerName};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
        System.out.println("Follow notification sent: " + Arrays.toString(message));
    }

    public void sendLoginNotification(String userId, String device, String location) {
        String[] message = {"Login", userId};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
        System.out.println("Login notification sent: " + Arrays.toString(message));
    }

    public void sendUserRegistrationNotification(String userId, String email) {
        String[] message = {"Register", userId, email};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
        System.out.println("Welcome notification sent to new user: " + email);
    }
}
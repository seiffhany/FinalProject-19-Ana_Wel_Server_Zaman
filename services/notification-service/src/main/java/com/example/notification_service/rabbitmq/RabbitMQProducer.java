package com.example.notification_service.rabbitmq;

import com.example.notification_service.factory.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendNotification(Notification notification) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.USER_ROUTING_KEY, notification);
    }
}

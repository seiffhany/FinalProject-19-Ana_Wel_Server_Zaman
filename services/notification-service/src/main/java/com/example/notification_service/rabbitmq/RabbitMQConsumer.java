package com.example.notification_service.rabbitmq;

import com.example.notification_service.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    @Autowired
    NotificationService notificationService;
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(String[] message) {
        notificationService.sendNotification(message);
        System.out.println("Received message: " + message);
    }
}

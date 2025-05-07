package com.example.notification_service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}

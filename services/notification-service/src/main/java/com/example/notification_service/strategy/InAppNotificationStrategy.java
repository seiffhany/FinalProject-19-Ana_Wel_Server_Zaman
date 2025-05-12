package com.example.notification_service.strategy;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.rabbitmq.RabbitMQProducer;

public class InAppNotificationStrategy implements NotificationStrategy{
    @Override
    public void sendNotification(Notification notification) {
        RabbitMQProducer rabbitMQProducer = new RabbitMQProducer();
        rabbitMQProducer.sendNotification(notification);
    }
}

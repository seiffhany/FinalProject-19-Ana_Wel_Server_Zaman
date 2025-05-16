package com.example.notification_service.strategy;

import com.example.notification_service.factory.Notification;

public class NotificationSender {
    private NotificationStrategy notificationStrategy;

    public void setStrategy(NotificationStrategy notificationStrategy) {
        this.notificationStrategy = notificationStrategy;
    }

    public void sendNotification(Notification notification) {
        notificationStrategy.sendNotification(notification);
    }
}
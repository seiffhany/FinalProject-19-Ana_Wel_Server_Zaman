package com.example.notification_service.strategy;

import com.example.notification_service.factory.Notification;

public interface NotificationStrategy {
    void sendNotification(Notification notification);
}

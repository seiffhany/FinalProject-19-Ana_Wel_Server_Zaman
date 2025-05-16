package com.example.notification_service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.repositories.NotificationRepository;

@Component
public class InAppNotificationStrategy implements NotificationStrategy {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendNotification(Notification notification) {
        // Save the notification to the database
        notificationRepository.save(notification);
    }
}

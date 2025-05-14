package com.example.notification_service.services;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.factory.NotificationFactory;
import com.example.notification_service.repositories.NotificationRepository;
import com.example.notification_service.strategy.EmailNotificationStrategy;
import com.example.notification_service.strategy.InAppNotificationStrategy;
import com.example.notification_service.strategy.NotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public ArrayList<Notification> getAllNotifications(String recipientId) {
        return new ArrayList<>(notificationRepository.findAllByRecipientIdAndTypeAndArchivedIsFalse(recipientId,"InAppNotification"));
    }
    public ArrayList<Notification> getUnreadNotifications(String recipientId) {
        return new ArrayList<>(notificationRepository.findAllByRecipientIdAndTypeAndArchivedIsFalseAndReadIsFalse(recipientId,"InAppNotification"));
    }
    public void markAsRead(String id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
    public void archiveNotification(String id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setArchived(true);
            notificationRepository.save(notification);
        }
    }
    public ArrayList<Notification> getArchivedNotifications(String recipientId) {
        return new ArrayList<>(notificationRepository.findAllByRecipientIdAndTypeAndArchivedIsTrue(recipientId,"InAppNotification"));
    }

}

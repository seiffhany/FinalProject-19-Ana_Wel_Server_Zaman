package com.example.notification_service.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.factory.NotificationFactory;
import com.example.notification_service.factory.NotificationType;
import com.example.notification_service.repositories.NotificationRepository;
import com.example.notification_service.strategy.EmailNotificationStrategy;
import com.example.notification_service.strategy.InAppNotificationStrategy;
import com.example.notification_service.strategy.NotificationSender;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public ArrayList<Notification> getAllNotifications(String recipientId) {
        return new ArrayList<>(
                notificationRepository.findAllByRecipientIdAndTypeAndArchivedIsFalse(recipientId,
                        NotificationType.IN_APP_NOTIFICATION));
    }

    public ArrayList<Notification> getUnreadNotifications(String recipientId) {
        return new ArrayList<>(notificationRepository
                .findAllByRecipientIdAndTypeAndArchivedIsFalseAndReadIsFalse(recipientId,
                        NotificationType.IN_APP_NOTIFICATION));
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
        return new ArrayList<>(
                notificationRepository.findAllByRecipientIdAndTypeAndArchivedIsTrue(recipientId,
                        NotificationType.IN_APP_NOTIFICATION));
    }

    public void sendNotification(String[] message) {
        // message = [type, recipientEmail]

        // Setting up the notification using the factory
        String type = message[0].split(" ")[0];
        Notification notification = NotificationFactory.createNotification(type, message[1]);
        notification.formulateNotificationMessage(message);

        // Send In-App
        NotificationSender sender = new NotificationSender();
        if (notification.getType() == NotificationType.IN_APP_NOTIFICATION) {
            sender.setStrategy(new InAppNotificationStrategy());
        }
        // Send via Email
        else {
            sender.setStrategy(new EmailNotificationStrategy());
        }
        sender.sendNotification(notification);
    }

}

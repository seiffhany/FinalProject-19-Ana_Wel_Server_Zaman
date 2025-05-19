package com.example.notification_service.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notification_service.exception.NotificationNotFoundException;
import com.example.notification_service.factory.Notification;
import com.example.notification_service.factory.NotificationFactory;
import com.example.notification_service.repositories.NotificationRepository;
import com.example.notification_service.strategy.EmailNotificationStrategy;
import com.example.notification_service.strategy.InAppNotificationStrategy;
import com.example.notification_service.strategy.NotificationSender;
import com.example.notification_service.utils.NotificationMessage;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private InAppNotificationStrategy inAppNotificationStrategy;

    @Autowired
    private EmailNotificationStrategy emailNotificationStrategy;

    public ArrayList<Notification> getAllNotifications(String recipientEmail) {
        return new ArrayList<>(notificationRepository.findAllByRecipientEmailAndIsArchivedIsFalse(recipientEmail));
    }

    public ArrayList<Notification> getUnreadNotifications(String recipientEmail) {
        return new ArrayList<>(
                notificationRepository.findAllByRecipientEmailAndIsArchivedIsFalseAndIsReadIsFalse(recipientEmail));
    }

    public void markAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void archiveNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notification.setArchived(true);
        notificationRepository.save(notification);
    }

    public ArrayList<Notification> getArchivedNotifications(String recipientEmail) {
        return new ArrayList<>(notificationRepository.findAllByRecipientEmailAndIsArchivedIsTrue(recipientEmail));
    }

    public void sendNotification(NotificationMessage message) {
        for (String recipientEmail : message.getRecipientEmails()) {
            Notification notification = NotificationFactory.createNotification(
                    message.getCategory(),
                    recipientEmail);

            String[] contentItems = message.getContent().split("\\|");
            String content = notification.formulateNotificationMessage(contentItems, message.getCategory());

            notification.setMessage(content);

            // Send notification based on type
            NotificationSender sender = new NotificationSender();
            switch (message.getType()) {
                case IN_APP_NOTIFICATION -> sender.setStrategy(inAppNotificationStrategy);
                case EMAIL_NOTIFICATION -> sender.setStrategy(emailNotificationStrategy);
                case EMAIL_AND_IN_APP_NOTIFICATION -> {
                    sender.setStrategy(inAppNotificationStrategy);
                    sender.sendNotification(notification);
                    sender.setStrategy(emailNotificationStrategy);
                }
            }
            sender.sendNotification(notification);
        }
    }
}

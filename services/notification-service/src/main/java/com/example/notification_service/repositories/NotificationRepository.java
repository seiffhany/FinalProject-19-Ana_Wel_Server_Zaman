package com.example.notification_service.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.factory.NotificationType;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByRecipientEmailAndTypeAndArchivedIsTrue(String recipientEmail, NotificationType type);

    List<Notification> findAllByRecipientEmailAndTypeAndArchivedIsFalse(String recipientEmail, NotificationType type);

    List<Notification> findAllByRecipientEmailAndTypeAndArchivedIsFalseAndReadIsFalse(String recipientEmail,
            NotificationType type);
}

package com.example.notification_service.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.notification_service.factory.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByRecipientEmailAndIsArchivedIsTrue(String recipientEmail);

    List<Notification> findAllByRecipientEmailAndIsArchivedIsFalse(String recipientEmail);

    List<Notification> findAllByRecipientEmailAndIsArchivedIsFalseAndIsReadIsFalse(String recipientEmail);
}

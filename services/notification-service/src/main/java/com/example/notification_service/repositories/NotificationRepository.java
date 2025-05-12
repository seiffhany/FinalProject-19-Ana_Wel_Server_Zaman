package com.example.notification_service.repositories;

import com.example.notification_service.factory.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByRecipientIdAndArchivedIsTrue(String recipientId);

    List<Notification> findAllByRecipientIdAndArchivedIsFalse(String recipientId);
    List<Notification> findAllByRecipientIdAndArchivedIsFalseAndReadIsFalse(String recipientId);
}

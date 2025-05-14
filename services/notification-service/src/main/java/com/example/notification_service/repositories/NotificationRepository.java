package com.example.notification_service.repositories;

import com.example.notification_service.factory.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByRecipientIdAndTypeAndArchivedIsTrue(String recipientId, String type);

    List<Notification> findAllByRecipientIdAndTypeAndArchivedIsFalse(String recipientId, String type);
    List<Notification> findAllByRecipientIdAndTypeAndArchivedIsFalseAndReadIsFalse(String recipientId, String type);
}

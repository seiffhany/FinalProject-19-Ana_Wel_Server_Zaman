package com.example.notification_service.repositories;

import com.example.notification_service.factory.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientId(String recipientId);

    List<Notification> findAllByArchivedIsFalse();
    List<Notification> findAllByArchivedIsTrue();
    List<Notification> findAllByReadIsFalse();
}

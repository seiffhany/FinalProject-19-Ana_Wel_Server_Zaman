package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.utils.NotificationMessage;

@Document(collection = "Notifications")
public class Follow extends Notification {

    public Follow(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category) {
        this.setMessage(message[0] + " started following you.");
        return this.getMessage();
    }
}

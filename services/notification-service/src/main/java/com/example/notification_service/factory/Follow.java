package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Follow extends NotificationBase implements Notification {

    public Follow(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message) {
        this.setMessage(message[2] + " started following you.");
        return this.getMessage();
    }
}

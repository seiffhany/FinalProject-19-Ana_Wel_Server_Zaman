package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.utils.NotificationMessage;

@Document(collection = "Notifications")
public class Register extends Notification {

    public Register(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail, NotificationMessage.NotificationType.EMAIL_NOTIFICATION);
    }

    @Override
    public String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category) {
        this.setMessage("Welcome to Ana Wel Server Zaman! Your registration from email \"" + message[0]
                + " was successful. \n" +
                "We are excited to have you on board!");
        return this.getMessage();
    }
}

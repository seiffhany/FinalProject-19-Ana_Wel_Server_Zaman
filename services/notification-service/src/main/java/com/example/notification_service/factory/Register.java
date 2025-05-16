package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Register extends NotificationBase implements Notification {

    public Register(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message) {
        this.setMessage("Welcome to Ana Wel Server Zaman! Your registration from email \"" + message[2]
                + " was successful. \n" +
                "We are excited to have you on board!");
        return this.getMessage();
    }
}

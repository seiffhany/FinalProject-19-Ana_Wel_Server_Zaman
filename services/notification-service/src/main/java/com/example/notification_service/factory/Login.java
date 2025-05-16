package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Login extends NotificationBase implements Notification {

    public Login(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message) {
        this.setMessage("You have logged in at " + getTimestamp() + " .");
        return this.getMessage();
    }
}

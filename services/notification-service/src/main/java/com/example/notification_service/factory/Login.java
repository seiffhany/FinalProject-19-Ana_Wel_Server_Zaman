package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.utils.NotificationMessage;

@Document(collection = "Notifications")
public class Login extends Notification {

    public Login(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail, NotificationMessage.NotificationType.EMAIL_NOTIFICATION);
    }

    @Override
    public String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category) {
        String device = message[0];
        String location = message[1];
        this.setMessage("You have logged in at " + getTimestamp() + " from " + device + " in " + location + ".");
        return this.getMessage();
    }
}

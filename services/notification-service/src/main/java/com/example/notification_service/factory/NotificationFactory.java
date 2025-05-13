package com.example.notification_service.factory;

import java.text.SimpleDateFormat;

public class NotificationFactory {
    public static Notification createNotification(String type, String recipientId) {
        String timestamp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new java.util.Date());
        switch (type) {
            case "Answer":
                return new Answer(timestamp, recipientId);
            case "Follow":
                return new Follow(timestamp, recipientId);
            case "Login":
                return new Login(timestamp, recipientId);
            case "Question":
                return new Question(timestamp, recipientId);
            case "Register":
                return new Register(timestamp, recipientId);
            default:
                return new Upvote_Downvote(timestamp, recipientId);
        }
    }
}

package com.example.notification_service.factory;

import java.text.SimpleDateFormat;

public class NotificationFactory {
    public static Notification createNotification(String type, String recipientEmail) {
        String timestamp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new java.util.Date());
        switch (type) {
            case "Answer":
                return new Answer(timestamp, recipientEmail);
            case "Follow":
                return new Follow(timestamp, recipientEmail);
            case "Login":
                return new Login(timestamp, recipientEmail);
            case "Question":
                return new Question(timestamp, recipientEmail);
            case "Register":
                return new Register(timestamp, recipientEmail);
            default:
                return new Upvote_Downvote(timestamp, recipientEmail);
        }
    }
}

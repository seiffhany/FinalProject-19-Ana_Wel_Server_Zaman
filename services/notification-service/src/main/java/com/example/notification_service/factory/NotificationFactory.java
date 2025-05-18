package com.example.notification_service.factory;

import com.example.notification_service.utils.NotificationMessage;

import java.text.SimpleDateFormat;


public class NotificationFactory {
    public static Notification createNotification(NotificationMessage.NotificationCategory category,
                                                  String recipientEmail) {
        String timestamp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new java.util.Date());
        switch (category) {
            case ANSWER_NEW:
            case ANSWER_ACCEPTED:
            case ANSWER_DELETED:
                return new Answer(timestamp, recipientEmail);
            case USER_FOLLOW:
                return new Follow(timestamp, recipientEmail);
            case USER_LOGIN:
                return new Login(timestamp, recipientEmail);
            case USER_REGISTRATION:
                return new Register(timestamp, recipientEmail);
            case QUESTION_NEW:
            case QUESTION_VIEW_COUNT:
                return new Question(timestamp, recipientEmail);
            case QUESTION_UPVOTE:
            case QUESTION_DOWNVOTE:
                return new Upvote_Downvote(timestamp, recipientEmail);
            default:
                throw new IllegalArgumentException("Unsupported notification category: " + category);
        }
    }
}

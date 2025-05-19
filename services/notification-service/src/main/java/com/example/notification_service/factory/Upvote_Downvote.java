package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.utils.NotificationMessage;

@Document(collection = "Notifications")
public class Upvote_Downvote extends Notification {

    public Upvote_Downvote(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category) {

        String questionContent = message[0];
        String userName = message[1];

        if (category == NotificationMessage.NotificationCategory.QUESTION_UPVOTE) {
            this.setMessage(userName + " upvoted your question: " + questionContent);
        } else {
            this.setMessage(userName + " downvoted your question: " + questionContent);
        }
        return this.getMessage();
    }
}

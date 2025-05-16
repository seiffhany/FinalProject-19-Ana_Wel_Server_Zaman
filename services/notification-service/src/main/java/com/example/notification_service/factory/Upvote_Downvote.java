package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Upvote_Downvote extends NotificationBase implements Notification {

    public Upvote_Downvote(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message) {
        if (message[0].equals("Upvote")) {
            this.setMessage(message[3] + " upvoted your question: " + message[2]);
        } else {
            this.setMessage(message[3] + " downvoted your question: " + message[2]);
        }
        return this.getMessage();
    }
}

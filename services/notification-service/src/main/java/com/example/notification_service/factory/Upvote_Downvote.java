package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Upvote_Downvote implements Notification{
    @Id
    private String id;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientId;
    private boolean isArchived;

    public Upvote_Downvote(String timestamp, String recipientId) {
        this.timestamp = timestamp;
        this.isRead = false;
        this.recipientId = recipientId;
        this.isArchived = false;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return "InAppNotification";
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getTimestamp() {
        return this.timestamp;
    }

    @Override
    public boolean isRead() {
        return this.isRead;
    }

    @Override
    public void setRead(boolean read) {
        this.isRead = read;
    }

    @Override
    public String getRecipientId() {
        return this.recipientId;
    }

    @Override
    public boolean isArchived() {
        return this.isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }
    @Override
    public String formulateNotificationMessage(String[] message) {
        if (message[0].equals("Upvote")) {
            this.message = message[3] + " upvoted your question: " + message[2];
        } else {
            this.message = message[3] + " downvoted your question: " + message[2];
        }
        return this.message;
    }
}

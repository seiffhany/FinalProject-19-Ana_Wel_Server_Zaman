package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Login implements Notification {
    @Id
    private String id;
    private String type;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientId;
    private boolean isArchived;

    public Login(String timestamp, String recipientId) {
        this.type = Login.class.getSimpleName();
        this.timestamp = timestamp;
        this.isRead = false;
        this.recipientId = recipientId;
        this.isArchived = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isRead() {
        return isRead;
    }

    @Override
    public void setRead(boolean read) {
        this.isRead = read;
    }

    @Override
    public String getRecipientId() {
        return recipientId;
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }
    @Override
    public String formulateNotificationMessage(String[] message) {
        this.message = "You have logged in ";
        return this.message;
    }
}

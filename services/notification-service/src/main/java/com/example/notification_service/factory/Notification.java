package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;

import com.example.notification_service.utils.NotificationMessage;

public abstract class Notification {
    @Id
    private String id;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientEmail;
    private boolean isArchived;

    public Notification(String timestamp, String recipientEmail) {
        this.timestamp = timestamp;
        this.isRead = false;
        this.recipientEmail = recipientEmail;
        this.isArchived = false;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }

    public abstract String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category);
}

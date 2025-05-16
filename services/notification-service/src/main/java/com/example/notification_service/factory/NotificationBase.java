package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;

public class NotificationBase {
    @Id
    private String id;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientEmail;
    private boolean isArchived;

    public NotificationBase(String timestamp, String recipientEmail) {
        this.timestamp = timestamp;
        this.isRead = false;
        this.recipientEmail = recipientEmail;
        this.isArchived = false;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return "EmailNotification";
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

    public String getRecpientEmail() {
        return recipientEmail;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }
}

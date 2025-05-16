package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;

public abstract class Notification {
    @Id
    private String id;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientEmail;
    private boolean isArchived;
    private NotificationType type;

    public Notification(String timestamp, String recipientEmail) {
        this.timestamp = timestamp;
        this.isRead = false;
        this.recipientEmail = recipientEmail;
        this.isArchived = false;
        this.type = NotificationType.EMAIL_NOTIFICATION;
    }

    public String getId() {
        return id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
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

    public abstract String formulateNotificationMessage(String[] message);
}

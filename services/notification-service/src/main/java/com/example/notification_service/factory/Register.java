package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Register implements Notification{
    @Id
    private String id;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientId;
    private boolean isArchived;

    public Register(String timestamp, String recipientId) {
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
        return "EmailNotification";
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
    public String formulateNotificationMessage(String[]message){
        this.message = "Welcome to Ana Wel Server Zaman! Your registration from email \""+ message[2] +" was successful. \n" +
                "We are excited to have you on board!";
        return this.message;
    }
}

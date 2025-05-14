package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Question implements Notification {
    @Id
    private String id;
    private String message;
    private String timestamp;
    private boolean isRead;
    private String recipientId;
    private boolean isArchived;

    public Question(String timestamp, String recipientId) {
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
        return "InAppNotification";
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
        isRead = read;
    }

    @Override
    public String getRecipientId() {
        return recipientId;
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
        String[] type = message[0].split(" ");
        if (type[1].equals("New")){
            this.message= message[3] + "has posted a new question: " + message[2];
        }
        else{
            this.message= "Your question: "+ message[2] + "has reached "+ message[3] + " views";
        }
        return this.message;
    }
}

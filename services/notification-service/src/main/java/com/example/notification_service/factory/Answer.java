package com.example.notification_service.factory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Answer implements Notification {
    @Id
    private String id;
    private String type;
    private String message;
    private final String timestamp;
    private boolean isRead;
    private String recipientId;

    private boolean isArchived;

    public Answer(String timestamp, String recipientId) {
        this.type = Answer.class.getSimpleName();
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
        String[] type = message[0].split(" ");
        if(type.length==1) {
            this.message = "You have a new answer from " + message[4] + " on your question: " + message[2] + "\n" +
                    "Answer: " + message[3];
        }
        else if (type[1].equals("Accepted")){
            this.message = "Your answer has on the question: " + message[2];
        }
        else {
            this.message = "Your answer: " + message[2]+ " has been deleted";
        }
        return this.message;
    }
}

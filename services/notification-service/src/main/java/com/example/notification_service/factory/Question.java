package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Question extends Notification {

    public Question(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message) {
        String[] type = message[0].split(" ");
        if (type[1].equals("New")) {
            this.setMessage(message[3] + "has posted a new question: " + message[2]);
        } else {
            this.setMessage("Your question: " + message[2] + "has reached " + message[3] + " views");
        }
        return this.getMessage();
    }
}

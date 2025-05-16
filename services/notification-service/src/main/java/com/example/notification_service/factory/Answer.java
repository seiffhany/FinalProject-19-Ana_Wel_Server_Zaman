package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")
public class Answer extends Notification {

    public Answer(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message) {
        String[] type = message[0].split(" ");
        if (type.length == 1) {
            this.setMessage("You have a new answer from " + message[4] + " on your question: " + message[2] + "\n" +
                    "Answer: " + message[3]);
        } else if (type[1].equals("Accepted")) {
            this.setMessage("Your answer has on the question: " + message[2]);
        } else {
            this.setMessage("Your answer: " + message[2] + " has been deleted");
        }
        return this.getMessage();
    }
}

package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.utils.NotificationMessage;

@Document(collection = "Notifications")
public class Question extends Notification {

    public Question(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail);
    }

    @Override
    public String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category) {

        String questionContent = message[0];

        switch (category) {
            case QUESTION_NEW:
                String userName = message[1];
                this.setMessage(userName + " has posted a new question: " + questionContent);
                break;
            case QUESTION_VIEW_COUNT:
                String viewCount = message[1];
                this.setMessage("Your question: " + questionContent + " has reached " + viewCount + " views");
                break;
            default:
                break;
        }

        return this.getMessage();
    }
}

package com.example.notification_service.factory;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.utils.NotificationMessage;

@Document(collection = "Notifications")
public class Answer extends Notification {

    public Answer(String timestamp, String recipientEmail) {
        super(timestamp, recipientEmail, NotificationMessage.NotificationType.EMAIL_NOTIFICATION);
    }

    @Override
    public String formulateNotificationMessage(String[] message,
            NotificationMessage.NotificationCategory category) {
        String question = message[0];
        String answer;
        String userName;

        switch (category) {
            case ANSWER_NEW:
                answer = message[1];
                userName = message[2];
                this.setMessage("You have a new answer from " + userName + " on your question: " + question + "\n"
                        + "Answer: " + answer);
                break;
            case ANSWER_ACCEPTED:
                this.setMessage("Your answer has been accepted on the question: " + question);
                break;
            case ANSWER_DELETED:
                this.setMessage("Your answer on question: " + question + " has been deleted");
                break;
            default:
                break;
        }
        return this.getMessage();
    }
}

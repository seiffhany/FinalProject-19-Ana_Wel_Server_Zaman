package com.example.notification_service.exception;

public class EmailNotificationException extends NotificationException {
    public EmailNotificationException(String message) {
        super("Failed to send email notification: " + message);
    }

    public EmailNotificationException(String message, Throwable cause) {
        super("Failed to send email notification: " + message, cause);
    }
}
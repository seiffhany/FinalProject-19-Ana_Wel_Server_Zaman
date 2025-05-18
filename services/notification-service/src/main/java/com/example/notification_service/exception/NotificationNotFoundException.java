package com.example.notification_service.exception;

public class NotificationNotFoundException extends NotificationException {
    public NotificationNotFoundException(String id) {
        super("Notification not found with id: " + id);
    }
}
package com.example.notification_service.exception;

public class RabbitMQException extends NotificationException {
    public RabbitMQException(String message) {
        super("RabbitMQ Error: " + message);
    }

    public RabbitMQException(String message, Throwable cause) {
        super("RabbitMQ Error: " + message, cause);
    }
}
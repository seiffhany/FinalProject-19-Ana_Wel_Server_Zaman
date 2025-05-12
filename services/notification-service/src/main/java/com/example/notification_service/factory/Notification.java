package com.example.notification_service.factory;

public interface Notification {
    String getId();
    String getType();
    String getMessage();
    String getTimestamp();
    boolean isRead();
    void setRead(boolean read);
    String getRecipientId();
    boolean isArchived();
    void setArchived(boolean archived);
    String formulateNotificationMessage(String[] message);
}

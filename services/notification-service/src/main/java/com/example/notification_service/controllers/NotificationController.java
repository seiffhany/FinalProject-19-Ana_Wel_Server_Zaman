package com.example.notification_service.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{recipientEmail}")
    public ArrayList<Notification> getAllNotifications(@PathVariable String recipientEmail) {
        return notificationService.getAllNotifications(recipientEmail);
    }

    @GetMapping("/unread/{recipientEmail}")
    public ArrayList<Notification> getUnreadNotifications(@PathVariable String recipientEmail) {
        return notificationService.getUnreadNotifications(recipientEmail);
    }

    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
    }

    @GetMapping("/archived/{recipientEmail}")
    public ArrayList<Notification> getArchivedNotifications(@PathVariable String recipientEmail) {
        return notificationService.getArchivedNotifications(recipientEmail);
    }

    @PutMapping("/archive/{id}")
    public void archiveNotification(@PathVariable String id) {
        notificationService.archiveNotification(id);
    }

}

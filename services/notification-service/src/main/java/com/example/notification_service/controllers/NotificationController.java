package com.example.notification_service.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ArrayList<Notification> getAllNotifications(@RequestHeader("userEmail") String recipientEmail) {
        return notificationService.getAllNotifications(recipientEmail);
    }

    @GetMapping("/unread")
    public ArrayList<Notification> getUnreadNotifications(@RequestHeader("userEmail") String recipientEmail) {
        return notificationService.getUnreadNotifications(recipientEmail);
    }

    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable String id, @RequestHeader("userEmail") String recipientEmail) {
        notificationService.markAsRead(id, recipientEmail);
    }

    @GetMapping("/archived")
    public ArrayList<Notification> getArchivedNotifications(@RequestHeader("userEmail") String recipientEmail) {
        return notificationService.getArchivedNotifications(recipientEmail);
    }

    @PutMapping("/archive/{id}")
    public void archiveNotification(@PathVariable String id, @RequestHeader("userEmail") String recipientEmail) {
        notificationService.archiveNotification(id, recipientEmail);
    }

}

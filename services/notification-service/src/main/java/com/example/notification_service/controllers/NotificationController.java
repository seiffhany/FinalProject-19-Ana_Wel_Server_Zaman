package com.example.notification_service.controllers;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ArrayList<Notification> getAllNotifications(@PathVariable String userId) {
        return notificationService.getAllNotifications(userId);
    }
    @GetMapping("/unread/{userId}")
    public ArrayList<Notification> getUnreadNotifications(@PathVariable String userId) {
        return notificationService.getUnreadNotifications(userId);
    }
    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
    }
    @GetMapping("/archived/{userId}")
    public ArrayList<Notification> getArchivedNotifications(@PathVariable String userId) {
        return notificationService.getArchivedNotifications(userId);
    }

    @PutMapping("/archive/{id}")
    public void archiveNotification(@PathVariable String id) {
        notificationService.archiveNotification(id);
    }

}

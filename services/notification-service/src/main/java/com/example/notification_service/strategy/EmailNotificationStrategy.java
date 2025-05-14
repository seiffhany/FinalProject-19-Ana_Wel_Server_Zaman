package com.example.notification_service.strategy;

import com.example.notification_service.factory.Notification;
import com.example.notification_service.repositories.NotificationRepository;
import com.example.user_service.models.User;
import com.example.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailNotificationStrategy implements NotificationStrategy{
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    @Async
    public void sendNotification (Notification notification){
        User user = userRepository.findById(UUID.fromString(notification.getRecipientId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + notification.getRecipientId()));
        String email = user.getEmail();
        String subject = "Someone You Know Posted a New Question!!";
        if (notification.getMessage().split(" ")[0].equals("Your")){
            subject = "Your Question is getting Attention!!";
        } else if (notification.getMessage().split(" ")[0].equals("You")) {
            subject = "A Login was made on your Account.";
        } else if (notification.getMessage().split(" ")[0].equals("Welcome")) {
            subject = "You've Registered Successfully!!";
        }

        notificationRepository.save(notification);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(notification.getMessage());
        mailMessage.setFrom("ana.wel.server.zaman@gmail.com");
        mailSender.send(mailMessage);
    }
}
package com.example.user_service.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Represents the notification preferences of a user.
 * This entity stores the user's preferences for receiving notifications
 * related to different activities within the application.
 */
@Entity
@Table(
        name = "notification_preferences",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "activity_type"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreference {

    /**
     * The unique identifier for the notification preference.
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "id")
    private UUID id;

    /**
     * The type of activity for which the notification preference is set.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    /**
     * Indicates whether the user prefers to receive notifications via in-app messages.
     */
    @Column(name = "in_app_enabled", nullable = false)
    private boolean inAppEnabled = true;

    /**
     * Indicates whether the user prefers to receive notifications via email.
     */
    @Column(name = "email_enabled", nullable = false)
    private boolean emailEnabled = false;

    /**
     * The user associated with this notification preference.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Constructor to create a NotificationPreference with activity type, in-app and email preferences.
     *
     * @param activityType  The type of activity for which the notification preference is set.
     * @param inAppEnabled  Indicates whether the user prefers to receive notifications via in-app messages.
     * @param emailEnabled  Indicates whether the user prefers to receive notifications via email.
     */
    public NotificationPreference(ActivityType activityType, boolean inAppEnabled, boolean emailEnabled) {
        this.activityType = activityType;
        this.inAppEnabled = inAppEnabled;
        this.emailEnabled = emailEnabled;
    }

}

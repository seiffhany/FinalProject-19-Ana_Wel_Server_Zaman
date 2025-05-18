package com.example.shared_models.notification;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Notification type cannot be null")
    private NotificationType type;

    @NotNull(message = "Notification category cannot be null")
    private NotificationCategory category;

    @NotEmpty(message = "At least one recipient must be specified")
    @Size(min = 1, message = "At least one recipient must be specified")
    private List<String> recipientEmails;

    private String content;

    private LocalDateTime timestamp;

    public enum NotificationCategory {
        QUESTION_NEW,
        QUESTION_VIEW_COUNT,
        QUESTION_UPVOTE,
        QUESTION_DOWNVOTE,
        ANSWER_NEW,
        ANSWER_ACCEPTED,
        ANSWER_DELETED,
        USER_FOLLOW,
        USER_LOGIN,
        USER_REGISTRATION
    }

    public enum NotificationType {
        EMAIL_NOTIFICATION,
        IN_APP_NOTIFICATION,
        EMAIL_AND_IN_APP_NOTIFICATION
    }

    @Override
    public String toString() {
        return String.format("NotificationMessage{type=%s, category=%s, recipients=%s, content='%s', timestamp=%s}",
                type, category, recipientEmails, content, timestamp);
    }

    public static class Builder {
        private NotificationType type;
        private NotificationCategory category;
        private List<String> recipientEmails;
        private String content;
        private LocalDateTime timestamp = LocalDateTime.now();

        public Builder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public Builder category(NotificationCategory category) {
            this.category = category;
            return this;
        }

        public Builder recipientEmails(List<String> recipientEmails) {
            this.recipientEmails = recipientEmails;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public NotificationMessage build() {
            NotificationMessage message = new NotificationMessage();
            message.type = this.type;
            message.category = this.category;
            message.recipientEmails = this.recipientEmails;
            message.content = this.content;
            message.timestamp = this.timestamp;
            return message;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
package com.example.answer_service.utils;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    @NotNull(message = "Notification type cannot be null")
    @JsonProperty("type")
    private NotificationType type;

    @NotNull(message = "Notification category cannot be null")
    @JsonProperty("category")
    private NotificationCategory category;

    @NotEmpty(message = "At least one recipient must be specified")
    @Size(min = 1, message = "At least one recipient must be specified")
    @JsonProperty("recipientEmails")
    private List<String> recipientEmails;

    @JsonProperty("content")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("timestamp")
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
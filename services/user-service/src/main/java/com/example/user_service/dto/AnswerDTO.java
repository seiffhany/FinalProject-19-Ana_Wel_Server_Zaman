package com.example.user_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AnswerDTO {
    private UUID id;
    private String content;
    private UUID questionId;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
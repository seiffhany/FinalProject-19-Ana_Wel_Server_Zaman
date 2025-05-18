package com.example.user_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class QuestionDTO {
    private UUID id;
    private String title;
    private String content;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
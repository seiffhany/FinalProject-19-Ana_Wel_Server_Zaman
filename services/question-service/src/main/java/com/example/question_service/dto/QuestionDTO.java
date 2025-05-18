package com.example.question_service.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class QuestionDTO {
    private UUID id;
    private String title;
    private String body;
}

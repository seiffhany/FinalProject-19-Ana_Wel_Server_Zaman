package com.example.user_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class QuestionDTO {
    private UUID id;
    private String title;
    private String body;
    private String authorId;
    private int voteCount;
    private List<String> tags = new ArrayList<>();
    private int viewCount;
}

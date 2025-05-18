package com.example.user_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class AnswerDTO {
    private UUID id;
    private UUID parentID;
    private UUID questionID;
    private String content;
    private boolean isBestAnswer;
    private int upVoteCount;
    private int downVoteCount;
}

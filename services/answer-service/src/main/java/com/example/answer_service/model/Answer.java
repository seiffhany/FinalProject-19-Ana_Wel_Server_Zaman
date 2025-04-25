package com.example.answer_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("Answer")

public class Answer {
    @Id
    private UUID id;
    private UUID questionID;
    private UUID userId;
    private String content;
    private boolean isAccepted;
    private int upVoteCount;
    private int downVoteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Answer(){
        this.id = UUID.randomUUID();
    }

    public Answer(UUID questionID, UUID userId, String content) {
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
        this.isAccepted = false;
        this.upVoteCount = 0;
        this.downVoteCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Answer(UUID questionID, UUID userId, String content, boolean isAccepted, int upVoteCount, int downVoteCount) {
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
        this.isAccepted = isAccepted;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getQuestionID() {
        return questionID;
    }

    public void setQuestionID(UUID questionID) {
        this.questionID = questionID;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

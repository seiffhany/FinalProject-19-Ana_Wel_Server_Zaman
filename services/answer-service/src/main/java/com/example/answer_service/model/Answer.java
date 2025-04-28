package com.example.answer_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Document("Answer")
public class Answer {
    @Id
    private UUID id;
    private UUID parentID;
    private UUID questionID;
    private UUID userId;
    private String content;
    private byte[] imageInByte;
    private boolean isBestAnswer;
    private int upVoteCount;
    private int downVoteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UUID> relatedAnswerIds;

    public Answer() {
        this.id = UUID.randomUUID();
    }

    public Answer(UUID questionID, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Answer(UUID questionID, UUID userId, String content, byte[] imageInByte) {
        this.id = UUID.randomUUID();
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
        this.imageInByte = imageInByte;
        this.createdAt = LocalDateTime.now();
    }

    public Answer(UUID parentID, UUID questionID, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.parentID = parentID;
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Answer(UUID parentID, UUID questionID, UUID userId, String content, byte[] imageInByte) {
        this.id = UUID.randomUUID();
        this.parentID = parentID;
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
        this.imageInByte = imageInByte;
        this.createdAt = LocalDateTime.now();
    }

    public List<UUID> getRelatedAnswerIds() {
        return relatedAnswerIds;
    }

    public void setRelatedAnswerIds(List<UUID> relatedAnswerIds) {
        this.relatedAnswerIds = relatedAnswerIds;
    }

    public UUID getId() {
        return id;
    }

    public UUID getParentID() {
        return parentID;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
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

    public byte[] getImageInByte() {
        return imageInByte;
    }

    public void setImageInByte(byte[] imageInByte) {
        this.imageInByte = imageInByte;
    }

    public boolean isBestAnswer() {
        return isBestAnswer;
    }

    public void setBestAnswer(boolean bestAnswer) {
        isBestAnswer = bestAnswer;
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

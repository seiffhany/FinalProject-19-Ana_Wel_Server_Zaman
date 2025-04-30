package com.example.answer_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document("Answer")
public class Answer {
    @Id
    private final UUID id;
    private UUID parentID;
    private UUID questionID;
    private UUID userId;
    private String content;
    private boolean isBestAnswer;
    private Set<UUID> upVoters = new HashSet<>();
    private Set<UUID> downVoters = new HashSet<>();
    private int upVoteCount;
    private int downVoteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Answer() {
        this.id = UUID.randomUUID();

        this.createdAt = LocalDateTime.now();
    }

    public Answer(UUID questionID, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.questionID = questionID;
        this.userId = userId;
        this.content = content;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<UUID> getUpVoters() {
        return upVoters;
    }

    public void setUpVoters(Set<UUID> upVoters) {
        this.upVoters = upVoters;
    }

    public Set<UUID> getDownVoters() {
        return downVoters;
    }

    public void setDownVoters(Set<UUID> downVoters) {
        this.downVoters = downVoters;
    }



    // Helper methods
    public void addUpVoter(UUID userId) {
        this.upVoters.add(userId);
        upVoteCount = upVoters.size();
    }
    public void addDownVoter(UUID userId) {
        this.downVoters.add(userId);
        downVoteCount = this.downVoters.size();
    }
    public void removeUpVoter(UUID userId) {
        this.upVoters.remove(userId);
        upVoteCount = this.upVoters.size();
    }
    public void removeDownVoter(UUID userId) {
        this.downVoters.remove(userId);
        downVoteCount = this.downVoters.size();
    }
}

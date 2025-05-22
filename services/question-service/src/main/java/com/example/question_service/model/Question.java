package com.example.question_service.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String body;
    private UUID authorId;
    private int voteCount;

    @ElementCollection
    private Set<UUID> upVoters = new HashSet<>();

    @ElementCollection
    private Set<UUID> downVoters = new HashSet<>();

    // relation with answers?

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    private int viewCount;
    private int answerCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper methods for vote management
    public void addUpVoter(UUID userId) {
        this.upVoters.add(userId);
        this.voteCount = this.upVoters.size() - this.downVoters.size();
    }

    public void addDownVoter(UUID userId) {
        this.downVoters.add(userId);
        this.voteCount = this.upVoters.size() - this.downVoters.size();
    }

    public void removeUpVoter(UUID userId) {
        this.upVoters.remove(userId);
        this.voteCount = this.upVoters.size() - this.downVoters.size();
    }

    public void removeDownVoter(UUID userId) {
        this.downVoters.remove(userId);
        this.voteCount = this.upVoters.size() - this.downVoters.size();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int votes) {
        this.voteCount = votes;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
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
package com.example.answer_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document("Answer")
public class Answer {
    @Id
    private UUID id;
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

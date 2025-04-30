package com.example.user_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Follower entity representing a follower-followed relationship between users.
 * This entity uses a composite key consisting of followerId and followedId.
 */
@Entity
@Table(name = "followers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follower {

    /**
     * Composite key for the Follower entity.
     */
    @EmbeddedId
    private FollowerId id;

    /**
     * The date and time when the follow relationship was created.
     */
    @Column(name = "followed_at", nullable = false)
    private OffsetDateTime followedAt = OffsetDateTime.now();

    /**
     * The user who is following another user.
     */
    @ManyToOne
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private User follower;

    /**
     * The user who is being followed.
     */
    @ManyToOne
    @JoinColumn(name = "followed_id", insertable = false, updatable = false)
    private User followed;

}

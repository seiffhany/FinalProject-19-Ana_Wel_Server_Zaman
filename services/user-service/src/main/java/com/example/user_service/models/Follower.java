package com.example.user_service.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@ToString
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
    @MapsId("followerId") // Maps the followerId in the composite key
    @JoinColumn(name = "follower_id")
    @JsonBackReference
    private User follower;

    /**
     * The user who is being followed.
     */
    @ManyToOne
    @MapsId("followedId") // Maps the followedId in the composite key
    @JoinColumn(name = "followed_id")
    @JsonBackReference
    private User followed;

    /**
     * Constructor to create a Follower entity with the follower and followed users.
     *
     * @param follower The user who is following another user.
     * @param followed The user who is being followed.
     */
    public Follower(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
        this.id = new FollowerId(follower.getId(), followed.getId());
        this.followedAt = OffsetDateTime.now();
    }

}

package com.example.user_service.models;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Follower entity representing a follower-followed relationship between users.
 * This entity uses a composite key consisting of followerId and followedId.
 */
@Entity
@Table(name = "followers")
@Getter
@Setter
@ToString
@EqualsAndHashCode
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
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @MapsId("followerId") // Maps the followerId in the composite key
    @JoinColumn(name = "follower_id")
    @JsonBackReference
    private User follower;

    /**
     * The user who is being followed.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
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

package com.example.user_service.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Composite key for the Follower entity.
 * This class represents the unique identifier for a follower-followed
 * relationship.
 */
@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FollowerId implements Serializable {

    /**
     * The ID of the user who is following another user.
     */
    @Column(name = "follower_id")
    private UUID followerId;

    /**
     * The ID of the user who is being followed.
     */
    @Column(name = "followed_id")
    private UUID followedId;
}

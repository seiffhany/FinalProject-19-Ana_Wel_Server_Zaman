package com.example.user_service.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * UserProfile entity representing a user's profile information.
 * This entity is linked to the User entity with a one-to-one relationship.
 */
@Entity
@Table(name = "user_profiles", indexes = {
        @Index(name = "idx_user_profile_user_id", columnList = "user_id"),
        @Index(name = "idx_user_profile_full_name", columnList = "full_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = { "id", "fullName", "location", "followerCount", "followingCount" })
public class UserProfile {

    /**
     * The unique identifier for the user profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    /**
     * The full name of the user.
     */
    @Column(name = "full_name", length = 100)
    private String fullName;

    /**
     * A short biography or description of the user.
     */
    @Column(name = "bio", length = 500)
    private String bio;

    /**
     * The URL of the user's profile picture.
     */
    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl;

    /**
     * The location of the user.
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * The number of followers the user has.
     * This is stored in the database and updated when followers change.
     */
    @Column(name = "follower_count", nullable = false)
    @Builder.Default
    private int followerCount = 0;

    /**
     * The number of users the user is following.
     * This is stored in the database and updated when following changes.
     */
    @Column(name = "following_count", nullable = false)
    @Builder.Default
    private int followingCount = 0;

    /**
     * The user associated with this profile.
     */
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonManagedReference
    private User user;

    /**
     * Constructor to create a UserProfile entity with the user's profile
     * information.
     *
     * @param fullName          The full name of the user.
     * @param bio               A short biography or description of the user.
     * @param profilePictureUrl The URL of the user's profile picture.
     * @param location          The location of the user.
     * @param user              The user associated with this profile.
     * @param followerCount     The initial number of followers.
     * @param followingCount    The initial number of following.
     */
    public UserProfile(String fullName,
            String bio,
            String profilePictureUrl,
            String location,
            User user,
            int followerCount,
            int followingCount) {
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.location = location;
        this.user = user;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}

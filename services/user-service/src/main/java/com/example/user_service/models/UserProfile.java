package com.example.user_service.models;

import com.example.user_service.models.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * UserProfile entity representing a user's profile information.
 * This entity is linked to the User entity with a one-to-one relationship.
 */
@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
     */
    @Column(name = "follower_count", nullable = false)
    private int followerCount;

    /**
     * The number of users the user is following.
     */
    @Column(name = "following_count", nullable = false)
    private int followingCount;

    /**
     * The date and time when the user profile was created.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Constructor to create a UserProfile entity with the user's profile information.
     *
     * @param fullName          The full name of the user.
     * @param bio               A short biography or description of the user.
     * @param profilePictureUrl The URL of the user's profile picture.
     * @param location          The location of the user.
     * @param followerCount     The number of followers the user has.
     * @param followingCount    The number of users the user is following.
     * @param user              The user associated with this profile.
     */
    public UserProfile(String fullName,
                       String bio,
                       String profilePictureUrl,
                       String location,
                       int followerCount,
                       int followingCount,
                       User user) {
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.location = location;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.user = user;
    }
}

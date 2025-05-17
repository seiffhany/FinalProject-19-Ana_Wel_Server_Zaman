package com.example.user_service.models;

import java.util.UUID;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * UserProfile entity representing a user's profile information.
 * This entity is linked to the User entity with a one-to-one relationship,
 * sharing the same primary key as the User entity.
 */
@Entity
@Table(name = "user_profiles", indexes = {
        @Index(name = "idx_user_profile_full_name", columnList = "full_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = { "userId", "fullName", "location" })
public class UserProfile {

    /**
     * The ID of the user profile, which is the same as the user's ID
     */
    @Id
    @Column(name = "user_id")
    private UUID userId;

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
     * This is calculated on-the-fly using a SQL query.
     * This field is read-only and cannot be modified directly.
     */
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.NONE)
    @Formula("(SELECT COUNT(*) FROM followers f WHERE f.followed_id = user_id)")
    @JsonProperty("followerCount")
    private final Long followerCount = 0L;

    /**
     * The number of users the user is following.
     * This is calculated on-the-fly using a SQL query.
     * This field is read-only and cannot be modified directly.
     */
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.NONE)
    @Formula("(SELECT COUNT(*) FROM followers f WHERE f.follower_id = user_id)")
    @JsonProperty("followingCount")
    private final Long followingCount = 0L;

    /**
     * The user associated with this profile.
     * The @MapsId annotation indicates that the user_id is both a foreign key and
     * the primary key.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
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
     */
    public UserProfile(String fullName,
            String bio,
            String profilePictureUrl,
            String location,
            User user) {
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.location = location;
        this.user = user;
        this.userId = user.getId(); // Set the ID from the user
    }

    /**
     * Builder class needs to be customized to handle final fields
     */
    public static class UserProfileBuilder {
        // These methods are no-ops since the fields are calculated
        public UserProfileBuilder followerCount(Long followerCount) {
            // No-op since this is a calculated field
            return this;
        }

        public UserProfileBuilder followingCount(Long followingCount) {
            // No-op since this is a calculated field
            return this;
        }
    }
}

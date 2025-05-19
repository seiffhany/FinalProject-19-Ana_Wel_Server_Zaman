package com.example.user_service.models;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * User entity representing a user in the system.
 * This class implements UserDetails for Spring Security integration.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "user_email_idx", columnList = "email"),
        @Index(name = "user_username_idx", columnList = "username")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    /**
     * The email address of the user.
     * This should be unique across all users.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The username of the user.
     * This should be unique across all users.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * The password of the user.
     * This should be stored securely (e.g., hashed).
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The role of the user.
     * This can be USER, ADMIN, or GUEST.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    /**
     * Tracks the time when the user was created.
     * This is automatically set to the current time when the user is created.
     */
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /**
     * Tracks the time when the user was last updated.
     * This is automatically set to the current time as when the user is updated.
     */
    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    /**
     * Tracks the time when the user last logged in.
     */
    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    /**
     * Tracks if the user is active for managing user accounts.
     * This can be used to deactivate a user without deleting their account.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    /**
     * The profile of the user.
     * This is a one-to-one relationship with the UserProfile entity.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private UserProfile userProfile;

    /**
     * The followers of this user.
     * This represents the users who are following this user.
     */
    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Follower> followers = new ArrayList<>();

    /**
     * The users that this user is following.
     * This represents the users that this user is following.
     */
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Follower> following = new ArrayList<>();

    /**
     * Constructor to create a User entity with the specified parameters.
     *
     * @param email       The email address of the user.
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param role        The role of the user (USER, ADMIN, GUEST).
     * @param createdAt   The date and time when the user was created.
     * @param updatedAt   The date and time when the user was last updated.
     * @param lastLoginAt The date and time when the user last logged in.
     * @param isActive    Indicates whether the user account is active or not.
     * @param userProfile The profile of the user.
     * @param followers   The list of followers of this user.
     * @param following   The list of users that this user is following.
     */
    public User(String email,
            String username,
            String password,
            Role role,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            OffsetDateTime lastLoginAt,
            boolean isActive,
            UserProfile userProfile,
            List<Follower> followers,
            List<Follower> following) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
        this.userProfile = userProfile;
        this.followers = followers;
        this.following = following;
    }

    /**
     * GrantedAuthority implementation for Spring Security.
     * This method returns the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority objects representing the user's
     *         roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Returns the password of the user.
     * This method is used for authentication purposes.
     *
     * @return The password of the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the email address of the user.
     * This method is used to retrieve the username for authentication.
     *
     * @return The email address of the user.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user account has expired.
     * This method returns true, indicating that the account is not expired.
     *
     * @return true if the account is not expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is locked.
     * This method returns true, indicating that the account is not locked.
     *
     * @return true if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * This method returns true, indicating that the credentials are not expired.
     *
     * @return true if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is enabled.
     * This method returns true, indicating that the account is enabled.
     *
     * @return true if the account is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public List<Follower> getFollowing() {
        return following;
    }

    public List<Follower> getFollowers() {
        return followers;
    }
}

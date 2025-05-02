package com.example.user_service.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * User entity representing a user in the system.
 * This class implements UserDetails for Spring Security integration.
 */
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "user_email_idx", columnList = "email"),
                @Index(name = "user_username_idx", columnList = "username")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    /**
     * The unique identifier for the user.
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
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
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /**
     * Tracks the time when the user was last updated.
     * This is automatically set to the current time as when the user is updated.
     */
    @Column(name = "updated_at", nullable = false)
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
    private boolean isActive = true;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private UserProfile userProfile;

    /**
     * The followers of this user.
     * This represents the users who are following this user.
     */
    @OneToMany(mappedBy = "followed")
    private List<Follower> followers = new ArrayList<>();

    /**
     * The users that this user is following.
     * This represents the users that this user is following.
     */
    @OneToMany(mappedBy = "follower")
    private List<Follower> following = new ArrayList<>();




    /**
     * GrantedAuthority implementation for Spring Security.
     * This method returns the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority objects representing the user's roles.
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
}

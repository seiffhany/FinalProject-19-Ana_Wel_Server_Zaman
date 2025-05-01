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
     * The bio of the user.
     * This is an optional field that can contain additional information about the user.
     */
    @Column(name = "bio")
    private String bio;

    /**
     * The URL of the user's profile picture.
     * This is an optional field that can contain a link to the user's profile image.
     */
    @Column(name = "profile_picture_url")
    private String profilePictureURL;

    /**
     * The roles associated with this user.
     * This defines what permissions the user has in the system.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            indexes = {
                    @Index(name = "user_roles_user_id_idx", columnList = "user_id")
            }
    )
    private List<Role> roles = new ArrayList<>();

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
     * The settings associated with this user.
     * This can include preferences for notifications, themes, etc.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_settings_id")
    private UserSettings userSettings;

    /**
     * The notification preferences associated with this user.
     * This defines how the user wants to receive notifications for different activities.
     */
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<NotificationPreference> notificationPreferences = new ArrayList<>();

    /**
     * Constructor to create a User with email, username, password, bio, and profile picture URL.
     *
     * @param email             The email address of the user.
     * @param username          The username of the user.
     * @param password          The password of the user.
     * @param bio               The bio of the user.
     * @param profilePictureURL  The URL of the user's profile picture.
     */
    public User(String email,
                String username,
                String password,
                String bio,
                String profilePictureURL
    ) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.profilePictureURL = profilePictureURL;
    }

    /**
     * GrantedAuthority implementation for Spring Security.
     * This method returns the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority objects representing the user's roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
                .collect(Collectors.toList());
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
        return true;
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
        return true;
    }
}

package com.example.user_service.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.factory.UserFactoryProvider;
import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import com.example.user_service.repositories.FollowerRepository;
import com.example.user_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final UserFactoryProvider userFactoryProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(String email, String username, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Get the appropriate factory and create the user
        var factory = userFactoryProvider.getFactory(role);
        User user = factory.createUser(username, email, passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User deactivateUser(UUID id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new IllegalArgumentException("User to deactivate not found");
        }

        user.setActive(false);
        user.setUpdatedAt(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User activateUser(UUID id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new IllegalArgumentException("User to activate not found");
        }

        user.setActive(true);
        user.setUpdatedAt(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User deleteUser(UUID id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new IllegalArgumentException("User to delete not found");
        }

        // Delete the user
        userRepository.delete(user);
        return user;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(UUID id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + user.getId()));

        // Update the user details
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setRole(user.getRole());
        existingUser.setUpdatedAt(OffsetDateTime.now());
        existingUser.setActive(user.isActive());

        return userRepository.save(existingUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllUsers() {
        List<User> users = userRepository.findAll();
        userRepository.deleteAll(users);
        log.info("All users deleted");
    }

    // private void updateFollowerCounts(User follower, User followed, boolean
    // isFollowing) {
    // // Update follower's following count
    // if (follower.getUserProfile() != null) {
    // UserProfile followerProfile = follower.getUserProfile();
    // followerProfile.setFollowingCount(
    // followerProfile.getFollowingCount() + (isFollowing ? 1 : -1));
    // userProfileRepository.save(followerProfile);
    // }

    // // Update followed user's follower count
    // if (followed.getUserProfile() != null) {
    // UserProfile followedProfile = followed.getUserProfile();
    // followedProfile.setFollowerCount(
    // followedProfile.getFollowerCount() + (isFollowing ? 1 : -1));
    // userProfileRepository.save(followedProfile);
    // }
    // }

}

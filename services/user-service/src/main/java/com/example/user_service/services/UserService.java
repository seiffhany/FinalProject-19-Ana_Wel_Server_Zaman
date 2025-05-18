package com.example.user_service.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.factory.UserFactory;
import com.example.user_service.factory.UserFactoryProvider;
import com.example.user_service.models.Follower;
import com.example.user_service.models.FollowerId;
import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
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
    public User createUser(String username, String email, String password, Role role) {
        log.info("Creating new user with role: {}", role);

        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Get the appropriate factory and create the user
        UserFactory factory = userFactoryProvider.getFactory(role);
        User user = factory.createUser(username, email, passwordEncoder.encode(password));

        // Save the user
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> getUserFollowers(UUID id) {
        return userRepository.getUserFollowers(id);
    }

    public List<User> getUserFollowing(UUID id) {
        return userRepository.getUserFollowing(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserProfile getUserProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new IllegalStateException("User profile not found");
        }

        return profile;
    }

    @Transactional
    public void followUser(UUID id, UUID followedId) {
        var followerOpt = userRepository.findById(id);
        var userToFollowOpt = userRepository.findById(followedId);

        if (userToFollowOpt.isEmpty() || followerOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        // Check if already following
        User follower = followerOpt.get();
        User userToFollow = userToFollowOpt.get();
        FollowerId followerRelationId = new FollowerId(follower.getId(), userToFollow.getId());
        if (followerRepository.existsById(followerRelationId)) {
            throw new IllegalStateException("Already following this user");
        }

        Follower followerRelation = new Follower(follower, userToFollow);
        followerRepository.save(followerRelation);

        // Update follower counts in user profiles
        // updateFollowerCounts(follower, userToFollow, true);
    }

    @Transactional
    public void unFollowUser(UUID id, UUID followerId) {
        User userToUnfollow = userRepository.findById(followerId).get();
        User follower = userRepository.findById(id).get();

        if (userToUnfollow == null || follower == null) {
            throw new IllegalArgumentException("User not found");
        }

        FollowerId followerRelationId = new FollowerId(follower.getId(), userToUnfollow.getId());
        if (!followerRepository.existsById(followerRelationId)) {
            throw new IllegalStateException("Not following this user");
        }

        followerRepository.deleteById(followerRelationId);

        // Update follower counts in user profiles
        // updateFollowerCounts(follower, userToUnfollow, false);
    }

    @Transactional
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
    public User deleteUser(UUID id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new IllegalArgumentException("User to delete not found");
        }

        // Delete the user
        userRepository.delete(user);
        return user;
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

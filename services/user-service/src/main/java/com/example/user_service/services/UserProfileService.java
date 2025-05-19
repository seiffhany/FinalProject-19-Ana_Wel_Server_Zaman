package com.example.user_service.services;

import java.util.List;
import java.util.UUID;

import com.example.user_service.clients.AnswerServiceClient;
import com.example.user_service.clients.QuestionServiceClient;
import com.example.user_service.dto.AnswerDTO;
import com.example.user_service.dto.QuestionDTO;
import com.example.user_service.dto.UserProfileDTO;
import com.example.user_service.models.Follower;
import com.example.user_service.models.FollowerId;
import com.example.user_service.rabbitmq.RabbitMQProducer;
import com.example.user_service.repositories.FollowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.config.RedisCacheConfig;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final RabbitMQProducer rabbitMQProducer;

    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }


    @Cacheable(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
    public UserProfile getProfileByUserId(UUID userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for user id: " + userId));
    }

    @Transactional
    public UserProfile createProfile(UUID userId, UserProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (userProfileRepository.existsById(userId)) {
            throw new IllegalStateException("User already has a profile");
        }

        profile.setUser(user);
        return userProfileRepository.save(profile);
    }

    @Transactional
    @CachePut(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
    public UserProfile updateProfile(UUID userId, UserProfile updatedProfile) {
        UserProfile existingProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for user id: " + userId));

        if (updatedProfile.getFullName() != null) {
            existingProfile.setFullName(updatedProfile.getFullName());
        }
        if (updatedProfile.getBio() != null) {
            existingProfile.setBio(updatedProfile.getBio());
        }
        if (updatedProfile.getProfilePictureUrl() != null) {
            existingProfile.setProfilePictureUrl(updatedProfile.getProfilePictureUrl());
        }
        if (updatedProfile.getLocation() != null) {
            existingProfile.setLocation(updatedProfile.getLocation());
        }

        return userProfileRepository.save(existingProfile);
    }

    @CacheEvict(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
    @Transactional
    public void deleteProfile(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for user id: " + userId));

        user.setUserProfile(null);
        userRepository.save(user);

        userProfileRepository.delete(profile);
    }

    public List<User> getUserFollowers(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        return userRepository.getUserFollowers(id);
    }

    public List<User> getUserFollowing(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return userRepository.getUserFollowing(id);
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

        // produce follow event
        rabbitMQProducer.sendFollowMessage(userToFollow.getEmail(), follower.getUsername());

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


}
package com.example.user_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.config.RedisCacheConfig;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;

@Service
public class UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final UserRepository userRepository;

  @Autowired
  public UserProfileService(
      UserProfileRepository userProfileRepository,
      UserRepository userRepository) {
    this.userProfileRepository = userProfileRepository;
    this.userRepository = userRepository;
  }

  public List<UserProfile> getAllProfiles() {
    return userProfileRepository.findAll();
  }

  @Cacheable(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
  public UserProfile getProfileById(UUID userId) {
    return userProfileRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found for user id: " + userId));
  }

  @Cacheable(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
  public UserProfile getProfileByUserId(UUID userId) {
    return getProfileById(userId);
  }

  @Transactional
  @CachePut(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
  public UserProfile createProfile(UUID userId, UserProfile profile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    if (userProfileRepository.existsById(userId)) {
      throw new IllegalStateException("User already has a profile");
    }

    profile.setUser(user);
    profile.setUserId(userId);
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

  @Transactional
  @CacheEvict(value = RedisCacheConfig.USER_PROFILE_CACHE, key = "#userId")
  public void deleteProfile(UUID userId) {
    UserProfile profile = userProfileRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found for user id: " + userId));
    userProfileRepository.delete(profile);
  }
}
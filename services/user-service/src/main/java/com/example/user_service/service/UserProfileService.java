package com.example.user_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;

@Service
public class UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final UserRepository userRepository;

  @Autowired
  public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
    this.userProfileRepository = userProfileRepository;
    this.userRepository = userRepository;
  }

  public List<UserProfile> getAllProfiles() {
    return userProfileRepository.findAll();
  }

  public UserProfile getProfileById(UUID id) {
    return userProfileRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));
  }

  public UserProfile getProfileByUserId(UUID userId) {
    return userProfileRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found with user id: " + userId));
  }

  @Transactional
  public UserProfile createProfile(UUID userId, UserProfile profile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    if (user.getUserProfile() != null) {
      throw new IllegalStateException("User already has a profile");
    }

    profile.setUser(user);
    return userProfileRepository.save(profile);
  }

  @Transactional
  public UserProfile updateProfile(UUID id, UserProfile updatedProfile) {
    UserProfile existingProfile = userProfileRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));

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
  public void deleteProfile(UUID id) {
    UserProfile profile = userProfileRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));
    userProfileRepository.delete(profile);
  }
}
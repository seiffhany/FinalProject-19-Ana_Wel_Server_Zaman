package com.example.user_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.UserProfileDTO;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.services.PublicUserProfileService;
import com.example.user_service.services.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.url}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final PublicUserProfileService publicUserProfileService;

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllProfiles() {
        return ResponseEntity.ok(userProfileService.getAllProfiles());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getFullProfileByUsername(@PathVariable String username) {
        return ResponseEntity.ok(publicUserProfileService.getFullProfileByUsername(username));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfile> getProfileByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(userProfileService.getProfileByUserId(userId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserProfile> createProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfile profile) {
        return ResponseEntity.ok(userProfileService.createProfile(userId, profile));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserProfile> updateProfile(
            @PathVariable UUID id,
            @RequestBody UserProfile profile) {
        return ResponseEntity.ok(userProfileService.updateProfile(id, profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followers/{id}")
    public List<User> getUserFollowers(@PathVariable UUID id) {
        return userProfileService.getUserFollowers(id);
    }

    @GetMapping("/following/{id}")
    public List<User> getUserFollowing(@PathVariable UUID id) {
        return userProfileService.getUserFollowing(id);
    }

    @PutMapping("/follow/{followedId}")
    public ResponseEntity<String> followUser(@RequestHeader("userId") UUID id, @PathVariable UUID followedId) {
        // return the userId
         userProfileService.followUser(id, followedId);
         return ResponseEntity.ok("Followed successfully");
    }

    @PutMapping("/unfollow/{followedId}")
    public ResponseEntity<String> unfollowUser(@RequestHeader("userId") UUID id, @PathVariable UUID followedId) {
        userProfileService.unFollowUser(id, followedId);
        return ResponseEntity.ok("Unfollowed successfully");
    }
}

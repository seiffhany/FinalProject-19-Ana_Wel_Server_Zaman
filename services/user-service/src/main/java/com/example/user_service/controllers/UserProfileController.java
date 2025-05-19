package com.example.user_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.models.UserProfile;
import com.example.user_service.services.UserProfileService;

@RestController
@RequestMapping("${api.base.url}/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllProfiles() {
        return ResponseEntity.ok(userProfileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfileById(@PathVariable UUID id) {
        return ResponseEntity.ok(userProfileService.getProfileById(id));
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

    @PutMapping("/{id}")
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
}

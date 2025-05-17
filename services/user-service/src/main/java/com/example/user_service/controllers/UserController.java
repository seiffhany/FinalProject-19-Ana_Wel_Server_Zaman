package com.example.user_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.models.User;
import com.example.user_service.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id).get();
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email).get();
    }

    @GetMapping("/{id}/followers")
    public List<User> getUserFollowers(@PathVariable UUID id) {
        return userService.getUserFollowers(id);
    }

    @GetMapping("/{id}/following")
    public List<User> getUserFollowing(@PathVariable UUID id) {
        return userService.getUserFollowing(id);
    }

    @PutMapping("/{id}/follow/{followedId}") // Will Change to use User Token
    public ResponseEntity<String> followUser(@PathVariable UUID id, @PathVariable UUID followedId) {
        userService.followUser(id, followedId);
        return ResponseEntity.ok("Followed successfully");
    }

    @PutMapping("/{id}/unfollow/{followedId}") // Will Change to use User Token
    public ResponseEntity<String> unfollowUser(@PathVariable UUID id, @PathVariable UUID followedId) {
        userService.unFollowUser(id, followedId);
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @PutMapping("/{id}/deactivate")
    public User deactivateUser(@PathVariable UUID id) {
        return userService.deactivateUser(id);
    }

    @PutMapping("/{id}/activate")
    public User activateUser(@PathVariable UUID id) {
        return userService.activateUser(id);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }
}

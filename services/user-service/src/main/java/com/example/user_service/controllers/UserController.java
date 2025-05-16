package com.example.user_service.controllers;

import com.example.user_service.models.User;
// import com.example.user_service.models.UserProfile;
import com.example.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // User endpoints
    @GetMapping
    public List<User> getAllUsers() {
         return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}/followers")
    public List<User> getUserFollowers(@PathVariable UUID id) {
        return userService.getUserFollowers(id);
    }

    @GetMapping("/{id}/following")
    public List<User> getUserFollowing(@PathVariable UUID id) {
        return userService.getUserFollowing(id);
    }

    @PutMapping("/{id}/follow/{followerId}")
    public ResponseEntity<String> followUser(@PathVariable UUID id, @PathVariable UUID followerId) {
        userService.followUser(id, followerId);
        return ResponseEntity.ok("Followed successfully");
    }

    @PutMapping("/{id}/unfollow/{followerId}")
    public ResponseEntity<String> unfollowUser(@PathVariable UUID id, @PathVariable UUID followerId) {
        userService.unFollowUser(id, followerId);
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @PutMapping("/{id}/deactivate")
    public User deactivateUser(@PathVariable UUID id){
        return userService.deactivateUser(id);
    }

    @PutMapping("/{id}/activate")
    public User activateUser(@PathVariable UUID id){
        return userService.activateUser(id);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id){
        return userService.deleteUser(id);
    }

}

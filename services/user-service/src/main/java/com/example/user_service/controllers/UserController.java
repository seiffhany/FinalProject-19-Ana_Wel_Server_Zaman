package com.example.user_service.controllers;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.user_service.models.User;

import com.example.user_service.services.UserService;


@RestController
@RequestMapping("${api.base.url}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id).get();
    }

    @GetMapping("/username/{username}")
    public User getUserByEmail(@PathVariable String username) {
        return userService.getUserByUsername(username).get();
    }


    @PutMapping("/{id}/deactivate")
    public User deactivateUser(@PathVariable UUID id) {
        return userService.deactivateUser(id);
    }

    @PutMapping("/{id}/activate")
    public User activateUser(@PathVariable UUID id) {
        return userService.activateUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/deleteAll")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "All users are deleted";
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }
}

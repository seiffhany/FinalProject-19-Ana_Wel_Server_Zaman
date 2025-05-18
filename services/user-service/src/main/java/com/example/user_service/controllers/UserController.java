package com.example.user_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.models.User;

import com.example.user_service.services.UserService;
import com.example.user_service.service.UserDataService;
import com.example.user_service.dto.QuestionDTO;
import com.example.user_service.dto.AnswerDTO;


@RestController
@RequestMapping("${api.base.url}")
public class UserController {

    private final UserService userService;
    private final UserDataService userDataService;

    public UserController(UserService userService, UserDataService userDataService) {
        this.userService = userService;
        this.userDataService = userDataService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id).get();
        List<QuestionDTO> userQuestions = userDataService.getUserQuestions(id);
        List<AnswerDTO> userAnswers = userDataService.getUserAnswers(id);
        return user;
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

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }
}

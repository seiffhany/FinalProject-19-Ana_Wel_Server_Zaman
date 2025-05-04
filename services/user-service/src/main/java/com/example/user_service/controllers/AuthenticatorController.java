package com.example.user_service.controllers;

import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticatorController handles authentication-related requests.
 * It provides an endpoint for user login.
 */
@RestController
@RequestMapping("${api.base.url}/auth")
@RequiredArgsConstructor
public class AuthenticatorController {

    private final AuthenticationService authenticationService; // Service for handling authentication logic

    /**
     * Handles user login requests.
     *
     * @param request the login request containing username and password
     * @return a response entity containing the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("Login request: " + request);
        // Call the authentication service to handle login
        return ResponseEntity.ok(authenticationService.login(request.getUsername(), request.getPassword()));
    }
}

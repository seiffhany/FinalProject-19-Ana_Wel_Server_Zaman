package com.example.user_service.controllers;

import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthenticatorController handles authentication-related requests.
 * It provides an endpoint for user login.
 */
@RestController
@RequestMapping("${api.base.url}/auth")
@RequiredArgsConstructor
@Slf4j
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

        // Log the incoming login request
        log.info("Login request received for username: {}", request.getUsername());

        // Call the authentication service to handle login
        return ResponseEntity.ok(authenticationService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {

        // Log the incoming logout request
        log.info("Logout request received for token: {}", token);

        // Call the authentication service to handle logout
        authenticationService.logout(token);

        // Return a response indicating successful logout
        return ResponseEntity.ok("You have been logged out successfully");
    }
}

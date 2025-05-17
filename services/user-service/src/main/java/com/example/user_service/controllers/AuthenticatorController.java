package com.example.user_service.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.RegisterRequest;
import com.example.user_service.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {

        // Log the incoming login request
        log.info("Register request received for username: {}", request.getUsername());

        // Call the authentication service to handle login
        return ResponseEntity.ok(authenticationService.register(request.getEmail(),
                request.getUsername(),
                request.getPassword(),
                request.getRole(),
                request.getFullName(),
                request.getBio(),
                request.getProfilePictureUrl(),
                request.getLocation())
        );
    }

    /**
     * Handles user logout requests.
     *
     * @param token the JWT token of the user to be logged out
     * @return a response entity indicating successful logout
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {

        // Log the incoming logout request
        log.info("Logout request received for token: {}", token);

        // Call the authentication service to handle logout
        authenticationService.logout(token);

        // Return a response indicating successful logout
        return ResponseEntity.ok("You have been logged out successfully");
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(
            @RequestHeader(value = "Authorization", required = false) String token) {
        // Log the incoming authentication request
        log.info("Authentication request received");

        // Call the authentication service to handle authentication
        Map<String, Object> response = authenticationService.authenticate(token);

        // Return a response indicating successful authentication
        return ResponseEntity.ok(response);
    }
}

package com.example.user_service.services;

import com.example.user_service.config.JwtTokenProvider;
import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * AuthenticationService class that handles user authentication.
 * It contains methods for login and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * This method handles user login.
     * It takes a username and password as input, authenticates the user,
     * and generates a JWT token for the user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return An AuthenticationResponse object containing the generated JWT token.
     */
    public AuthenticationResponse login(String username, String password) {
        // authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // find the user by username and check if the user is active
        var user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // generate a JWT token for the user
        var jwtToken = jwtTokenProvider.generateToken(user, user.getId());

        // return the authentication response with the token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

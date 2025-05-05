package com.example.user_service.services;

import com.example.user_service.config.JwtTokenProvider;
import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.models.User;
import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AuthenticationService class that handles user authentication.
 * It contains methods for login and registration.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDetailsService userDetailsService;

    // Redis key for storing the token blacklist
    private static final String TOKEN_BLACKLIST_NAME = "blacklist:";


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
        // if the user is not found or the password is incorrect, an exception will be thrown
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // log the successful authentication
        log.info("User {} authenticated successfully", username);

        // find the user by username and check if the user is active
        var user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // generate a JWT token for the user
        var jwtToken = jwtTokenProvider.generateToken(user, user.getId());

        // log the generated token
        log.info("Generated JWT token: {}", jwtToken);

        // return the authentication response with the token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * This method handles user logout.
     * It takes the JWT token from the Authorization header,
     * extracts the user ID, and blacklists the token in Redis.
     *
     * @param authorizationHeader The Authorization header containing the JWT token.
     */
    public void logout(String authorizationHeader) {
        String jwt = extractJwtFromHeader(authorizationHeader);

        if (jwt == null || jwt.isEmpty()) {
            log.error("JWT token is null or empty");
            throw new RuntimeException("JWT token is null or empty");
        }

        try {
            String username = jwtTokenProvider.extractUsername(jwt);
            if (username == null || username.isEmpty()) {
                throw new RuntimeException("Username is null or empty");
            }

            User user = (User) userDetailsService.loadUserByUsername(username);

            if (!jwtTokenProvider.isTokenValid(jwt, user)) {
                throw new RuntimeException("JWT token is invalid or expired");
            }

            UUID userId = jwtTokenProvider.extractUserId(jwt);
            Date expirationDate = jwtTokenProvider.extractExpiration(jwt);

            long ttlSeconds = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

            if (ttlSeconds <= 0) {
                throw new RuntimeException("JWT token is expired");
            }

            String redisKey = TOKEN_BLACKLIST_NAME + jwt;
            if (redisTemplate.hasKey(redisKey)) {
                throw new RuntimeException("JWT token is already blacklisted");
            }

            redisTemplate.opsForValue().set(redisKey, userId.toString(), ttlSeconds, TimeUnit.SECONDS);
            log.info("Boolean : " + redisTemplate.opsForValue().get(redisKey));
            log.info("Token blacklisted for user [{}] with TTL [{}] seconds", user.getUsername(), ttlSeconds);
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            throw new RuntimeException("Failed to process token during logout: " + e.getMessage());
        }


    }

    /**
     * This method extracts the JWT token from the Authorization header.
     * It checks if the header is not null and starts with "Bearer ".
     * If so, it extracts the token by removing the "Bearer " prefix.
     *
     * @param authorizationHeader The Authorization header containing the JWT token.
     * @return The extracted JWT token or null if not found.
     */
    private String extractJwtFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


}

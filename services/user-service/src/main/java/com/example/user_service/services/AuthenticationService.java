package com.example.user_service.services;

import com.example.user_service.config.JwtTokenProvider;
import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.factory.UserFactoryProvider;
import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
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
    private final UserProfileService userProfileService;
    private final UserService userService;

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
    @Transactional
    public AuthenticationResponse login(String username, String password) {
        // authenticate the user
        // if the user is not found or the password is incorrect, an exception will be
        // thrown
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        // log the successful authentication
        log.debug("User {} authenticated successfully", username);

        // find the user by username and check if the user is active
        var user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or inactive"));

        // update the last login time
        user.setLastLoginAt(OffsetDateTime.now());

        // save the user with the updated last login time
        userRepository.save(user);

        // generate a JWT token for the user
        var jwtToken = jwtTokenProvider.generateToken(user, user.getId(), user.getEmail());

        // log the generated token
        log.debug("Generated JWT token: {}", jwtToken);

        // return the authentication response with the token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * This method handles full user registration.
     * It creates a user profile given his required attributes,
     * and returns an instance of the created user profile.
     *
     * @param fullName          The name of the user.
     * @param bio               The bio of the user.
     * @param profilePictureUrl The profile picture url of the user.
     * @param location          The location of the user.
     * @return An AuthenticationResponse object containing the generated JWT token.
     */
    @Transactional
    public AuthenticationResponse register(String email, String username, String password, Role role, String fullName,
            String bio, String profilePictureUrl, String location) {
        // Create the user using the factory
        User user = userService.createUser(email, username, password, role);

        // Create the user profile using the UserProfileService
        UserProfile userProfile = UserProfile.builder()
                .fullName(fullName)
                .bio(bio)
                .profilePictureUrl(profilePictureUrl)
                .location(location)
                .build();

        userProfileService.createProfile(user.getId(), userProfile);

        // log the successful registration
        log.debug("User {} registered successfully", username);

        // generate a JWT token for the user
        var jwtToken = jwtTokenProvider.generateToken(user, user.getId(), user.getEmail());

        // log the generated token
        log.debug("Generated JWT token: {}", jwtToken);

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

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("No authenticated user found in security context");
            }

            String authenticatedUsername = authentication.getName();
            if (!username.equals(authenticatedUsername)) {
                throw new RuntimeException("JWT token does not belong to the authenticated user");
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
            log.info("Token blacklisted for user [{}] with TTL [{}] seconds", user.getUsername(), ttlSeconds);

        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            throw new RuntimeException("Failed to process token during logout: " + e.getMessage());
        }


    }

    /**
     * This method authenticates the user by checking the security context.
     * It retrieves the authentication object and checks if the user is
     * authenticated.
     * If authenticated, it returns a map containing the user ID and username.
     * It allows other services to access the authenticated user's information.
     *
     * @return A map containing the user ID and username.
     */
    public Map<String, Object> authenticate(String authorizationHeader) {

        // Ensure the Authorization header is not null or empty
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new RuntimeException("Missing Authorization header");
        }

        // Extract the JWT token from the Authorization header
        String jwt = extractJwtFromHeader(authorizationHeader);
        if (jwt == null || jwt.isEmpty()) {
            throw new RuntimeException("JWT token is null or empty");
        }

        // Check if the JWT token has already been validated and authentication is set in SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //  If the user is not authenticated, throw an error (this should normally not happen due to JwtAuthenticationFilter)
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found in security context");
        }

        // Extract the username from the authentication object
        String username = authentication.getName();

        // Check if the username is null or empty
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new RuntimeException("User not found");
        }

        return Map.of(
                "userId", ((User) userDetails).getId(),
                "username", userDetails.getUsername(),
                "roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
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

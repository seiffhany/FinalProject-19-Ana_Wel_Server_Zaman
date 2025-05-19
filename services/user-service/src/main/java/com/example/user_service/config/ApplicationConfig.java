package com.example.user_service.config;

import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * ApplicationConfig class that contains the configuration for the application.
 * It includes beans for user authentication and password encoding.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    /**
     * UserRepository instance to access user data.
     */
    private final UserRepository userRepository;

    /**
     * This method returns a UserDetailsService bean.
     * It is responsible for loading user-specific data.
     * It retrieves user details from the database using the UserRepository.
     *
     * @return UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * This method returns an AuthenticationProvider bean.
     * It is responsible for authenticating users.
     * It uses the DaoAuthenticationProvider to fetch user details and encode passwords.
     *
     * @return AuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * This method returns an AuthenticationManager bean.
     * It is responsible for authenticating users.
     * It uses the AuthenticationConfiguration to get the authentication manager.
     *
     * @param config The AuthenticationConfiguration object used to create the AuthenticationManager.
     * @return AuthenticationManager instance
     * @throws Exception If an error occurs while creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * This method returns a BCryptPasswordEncoder bean.
     * It is used to encode passwords using the BCrypt hashing algorithm.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package com.example.user_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfiguration class that configures the security settings for the
 * application.
 * It sets up the security filter chain, authentication provider, and JWT
 * authentication filter.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter; // JWT authentication filter
    private final AuthenticationProvider authenticationProvider; // authentication provider
    @Value("${api.base.url}")
    private String apiBaseUrl; // Base URL for the API

    /**
     * This method configures the security filter chain for the application.
     * It sets up the authentication provider, JWT authentication filter,
     * and specifies which endpoints are publicly accessible and which require
     * authentication.
     *
     * @param http The HttpSecurity object used to configure security settings.
     * @return The configured SecurityFilterChain object.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .authorizeHttpRequests(req -> req.requestMatchers(
                        apiBaseUrl + "/auth/login", // Permit login
                        apiBaseUrl + "/auth/register", // Permit authenticate endpoint
                        apiBaseUrl + "/seed/**", // Permit seed data
                        apiBaseUrl + "/profile/{username}",
                        apiBaseUrl + "/profile/user/**" // Permit profile endpoints
                )
                        .permitAll() // Allow all requests to the auth endpoint
                        .anyRequest()
                        .authenticated() // Require authentication for all other requests
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use
                                                                                                              // stateless
                                                                                                              // session
                                                                                                              // management
                                                                                                              // which
                                                                                                              // means
                                                                                                              // no
                                                                                                              // session
                                                                                                              // will be
                                                                                                              // created
                                                                                                              // or used
                                                                                                              // by
                                                                                                              // Spring
                                                                                                              // Security
                .authenticationProvider(authenticationProvider) // Set the authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT
                                                                                             // authentication filter
                                                                                             // before the
                                                                                             // UsernamePasswordAuthenticationFilter

        return http.build(); // Build the security filter chain
    }

}

package com.example.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JwtConfig class that provides configuration for JWT token generation and validation.
 * This class is responsible for creating a bean of JwtTokenProvider with the secret key and expiration time.
 */
@Configuration
public class JwtConfig {

    // Injecting the secret key and expiration time from application properties
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * This method creates a bean of JwtTokenProvider with the secret key and expiration time.
     * It uses the @Bean annotation to indicate that this method returns a bean to be managed by the Spring container.
     *
     * @return The JwtTokenProvider bean.
     */
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return JwtTokenProvider.getInstance(secretKey, expirationTime);
    }
}

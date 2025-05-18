package com.example.user_service.factory;

import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Factory for creating regular users
 */
@Component
public class RegularUserFactory implements UserFactory {

    @Override
    public User createUser(String username, String email, String password) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.USER)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .lastLoginAt(OffsetDateTime.now())
                .isActive(true)
                .build();
    }
}
package com.example.user_service.factory;

import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;

/**
 * Factory interface for creating different types of users
 */
public interface UserFactory {
    /**
     * Creates a new user with the specified details
     * 
     * @param username The username for the new user
     * @param email    The email for the new user
     * @param password The encoded password for the new user
     * @return A new User instance with appropriate role and settings
     */
    User createUser(String username, String email, String password);
}
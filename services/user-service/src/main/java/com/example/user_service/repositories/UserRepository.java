package com.example.user_service.repositories;

import com.example.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository interface that extends JpaRepository to provide CRUD operations for User entity.
 * It contains a method to find a user by username and check if the user is active.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameAndIsActiveTrue(String username); // find user by username and check if the user is active
    boolean existsByEmail(String email); // check if user with specified email exists.
    boolean existsByUsername(String username); // checks if user with specified username exists.
}

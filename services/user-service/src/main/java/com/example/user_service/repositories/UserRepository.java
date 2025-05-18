package com.example.user_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.user_service.models.User;

/**
 * UserRepository interface that extends JpaRepository to provide CRUD
 * operations for User entity.
 * It contains a method to find a user by username and check if the user is
 * active.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameAndIsActiveTrue(String username); // find user by username and check if the user is active

    boolean existsByEmail(String email); // check if user with specified email exists.

    boolean existsByUsername(String username); // checks if user with specified username exists.

    @Query("SELECT f.follower FROM Follower f WHERE f.followed.id = :id")
    List<User> getUserFollowers(@Param("id") UUID id);

    @Query("SELECT f.followed FROM Follower f WHERE f.follower.id = :id")
    List<User> getUserFollowing(@Param("id") UUID id);

}

package com.example.user_service.repositories;

import com.example.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * UserRepository interface that extends JpaRepository to provide CRUD
 * operations for User entity.
 * It contains a method to find a user by username and check if the user is
 * active.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT f.follower FROM Follower f WHERE f.followed.id = :id")
    List<User> getUserFollowers(@Param("id") UUID id);

    @Query("SELECT f.followed FROM Follower f WHERE f.follower.id = :id")
    List<User> getUserFollowing(@Param("id") UUID id);

    Optional<User> findByUsernameAndIsActiveTrue(String username);

    Optional<User> findByEmail(String email);
}

package com.example.user_service.repositories;

import com.example.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    
    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getUserById(@Param("id") UUID id);

    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    @Query("SELECT f.follower FROM Follower f WHERE f.followed.id = :id")
    List<User> getUserFollowers(@Param("id") UUID id);

    @Query("SELECT f.followed FROM Follower f WHERE f.follower.id = :id")
    List<User> getUserFollowing(@Param("id") UUID id);
}

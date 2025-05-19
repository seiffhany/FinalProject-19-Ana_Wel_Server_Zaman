package com.example.user_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;

/**
 * UserProfileRepository is an interface that extends JpaRepository to provide
 * CRUD operations for the UserProfile entity.
 * It uses UUID as the ID type for the UserProfile entity.
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

  @Query("SELECT up FROM UserProfile up WHERE up.user.id = :userId")
  Optional<UserProfile> findByUserId(@Param("userId") UUID userId);

  @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END FROM UserProfile up WHERE up.user = :user")
  boolean existsByUser(@Param("user") User user);
}

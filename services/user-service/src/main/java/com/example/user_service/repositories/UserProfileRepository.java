package com.example.user_service.repositories;

import com.example.user_service.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * UserProfileRepository is an interface that extends JpaRepository to provide CRUD operations for the UserProfile entity.
 * It uses UUID as the ID type for the UserProfile entity.
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}

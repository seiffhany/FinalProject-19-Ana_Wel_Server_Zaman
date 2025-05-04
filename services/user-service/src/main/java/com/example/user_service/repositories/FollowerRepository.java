package com.example.user_service.repositories;

import com.example.user_service.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * FollowerRepository is an interface that extends JpaRepository to provide CRUD operations for the Follower entity.
 * It uses UUID as the ID type for the Follower entity.
 */
@Repository
public interface FollowerRepository extends JpaRepository<Follower, UUID> {
}

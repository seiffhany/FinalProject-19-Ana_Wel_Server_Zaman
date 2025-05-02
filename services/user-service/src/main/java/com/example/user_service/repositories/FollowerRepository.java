package com.example.user_service.repositories;

import com.example.user_service.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowerRepository extends JpaRepository<Follower, UUID> {
}

package com.example.user_service.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.user_service.models.Follower;
import com.example.user_service.models.FollowerId;
import com.example.user_service.models.User;

public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {

    @Query("SELECT f FROM Follower f WHERE f.follower.id = :followerId AND f.followed.id = :followedId")
    Follower findByFollowerAndFollowed(@Param("followerId") UUID followerId, @Param("followedId") UUID followedId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.followed.id = :userId")
    long countFollowers(@Param("userId") UUID userId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.follower.id = :userId")
    long countFollowing(@Param("userId") UUID userId);

    @Query("SELECT f FROM Follower f WHERE f.follower.id = :userId")
    List<Follower> findAllByFollowerId(@Param("userId") UUID userId);

    @Query("SELECT f FROM Follower f WHERE f.followed.id = :userId")
    List<Follower> findAllByFollowedId(@Param("userId") UUID userId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follower f WHERE f.follower = :follower AND f.followed = :following")
    boolean existsByFollowerAndFollowing(@Param("follower") User follower, @Param("following") User following);
}

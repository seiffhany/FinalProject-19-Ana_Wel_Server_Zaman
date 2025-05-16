package com.example.user_service.service;

import com.example.user_service.models.Follower;
import com.example.user_service.models.FollowerId;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.FollowerRepository;
// import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    // private final UserProfileRepository userProfileRepository;

    public UserService(UserRepository userRepository, 
                      FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(UUID id) {
        return userRepository.getUserById(id);
    }

    public List<User> getUserFollowers(UUID id){
        return userRepository.getUserFollowers(id);
    }

    public List<User> getUserFollowing(UUID id){
        return userRepository.getUserFollowing(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }



    @Transactional
    public UserProfile getUserProfile(UUID userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new IllegalStateException("User profile not found");
        }
        
        return profile;
    }

    @Transactional
    public void followUser(UUID id, UUID followerId) {
        User userToFollow = userRepository.getUserById(id);
        User follower = userRepository.getUserById(followerId);
        
        if (userToFollow == null || follower == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Check if already following
        FollowerId followerRelationId = new FollowerId(follower.getId(), userToFollow.getId());
        if (followerRepository.existsById(followerRelationId)) {
            throw new IllegalStateException("Already following this user");
        }
        
        Follower followerRelation = new Follower(follower, userToFollow);
        followerRepository.save(followerRelation);
        
        // Update follower counts in user profiles
        // updateFollowerCounts(follower, userToFollow, true);
    }

    @Transactional
    public void unFollowUser(UUID id, UUID followerId) {
        User userToUnfollow = userRepository.getUserById(id);
        User follower = userRepository.getUserById(followerId);
        
        if (userToUnfollow == null || follower == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        FollowerId followerRelationId = new FollowerId(follower.getId(), userToUnfollow.getId());
        if (!followerRepository.existsById(followerRelationId)) {
            throw new IllegalStateException("Not following this user");
        }
        
        followerRepository.deleteById(followerRelationId);
        
        // Update follower counts in user profiles
        // updateFollowerCounts(follower, userToUnfollow, false);
    }

    @Transactional
    public User deactivateUser(UUID id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("User to deactivate not found");
        }
        
        user.setActive(false);
        user.setUpdatedAt(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User activateUser(UUID id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("User activate not found");
        }
        
        user.setActive(true);
        user.setUpdatedAt(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User deleteUser(UUID id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("User to delete not found");
        }
        
        // // Delete user profile if exists
        // if (user.getUserProfile() != null) {
        //     userProfileRepository.delete(user.getUserProfile());
        // }
        
        // Delete all follower relationships
        followerRepository.deleteAll(user.getFollowers());
        followerRepository.deleteAll(user.getFollowing());
        
        // Delete the user
        userRepository.delete(user);
        return user;
    }

}

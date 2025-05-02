package com.example.user_service.controllers;

import com.example.user_service.models.Follower;
import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.FollowerRepository;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("${api.base.url}/seed")
@RequiredArgsConstructor
public class DatabaseSeederController {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FollowerRepository followerRepository;

    @GetMapping
    public ResponseEntity<String> seedDatabase() {

        // Create users
        User user1 = User.builder()
                .email("alice@example.com")
                .username("alice")
                .password("hashedpassword1")
                .role(Role.USER)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .isActive(true)
                .build();

        User user2 = User.builder()
                .email("bob@example.com")
                .username("bob")
                .password("hashedpassword2")
                .role(Role.USER)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .isActive(true)
                .build();

        User user3 = User.builder()
                .email("charlie@example.com")
                .username("charlie")
                .password("hashedpassword3")
                .role(Role.ADMIN)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .isActive(true)
                .build();

        userRepository.saveAll(Arrays.asList(user1, user2, user3));

        // Create user profiles
        UserProfile profile1 = UserProfile.builder()
                .fullName("Alice Smith")
                .bio("Loves coding and cats.")
                .location("Cairo")
                .profilePictureUrl("https://example.com/images/alice.jpg")
                .followerCount(2)
                .followingCount(1)
                .user(user1)
                .build();

        UserProfile profile2 = UserProfile.builder()
                .fullName("Bob Johnson")
                .bio("Backend engineer and gamer.")
                .location("Giza")
                .profilePictureUrl("https://example.com/images/bob.jpg")
                .followerCount(1)
                .followingCount(2)
                .user(user2)
                .build();

        UserProfile profile3 = UserProfile.builder()
                .fullName("Charlie King")
                .bio("Admin of the platform.")
                .location("Alexandria")
                .profilePictureUrl("https://example.com/images/charlie.jpg")
                .followerCount(0)
                .followingCount(0)
                .user(user3)
                .build();

        userProfileRepository.saveAll(Arrays.asList(profile1, profile2, profile3));

        // Create follow relationships
        Follower f1 = new Follower(user1, user2); // Alice follows Bob
        Follower f2 = new Follower(user2, user1); // Bob follows Alice
        Follower f3 = new Follower(user2, user3); // Bob follows Charlie

        followerRepository.saveAll(Arrays.asList(f1, f2, f3));

        return ResponseEntity.ok("Database seeded successfully.");
    }
}

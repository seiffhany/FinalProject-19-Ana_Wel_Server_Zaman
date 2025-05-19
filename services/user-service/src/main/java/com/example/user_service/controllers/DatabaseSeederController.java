package com.example.user_service.controllers;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.config.JwtTokenProvider;
import com.example.user_service.models.Follower;
import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.FollowerRepository;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.url}/seed")
@RequiredArgsConstructor
public class DatabaseSeederController {

        private final UserRepository userRepository;
        private final UserProfileRepository userProfileRepository;
        private final FollowerRepository followerRepository;
        private final JwtTokenProvider jwtTokenProvider;

        @GetMapping
        public ResponseEntity<String> seedDatabase() {
                // Create users
                User user1 = User.builder()
                                .email("alice@example.com")
                                .username("alice")
                                .password("$2y$10$n7A4MDByokJkuTDd5AJXgea4juGbY6m2zvxo03BGEHCGreQhfvvZ.") // password
                                .role(Role.USER)
                                .createdAt(OffsetDateTime.now())
                                .updatedAt(OffsetDateTime.now())
                                .isActive(true)
                                .build();

                User user2 = User.builder()
                                .email("bob@example.com")
                                .username("bob")
                                .password("$2y$10$HyWts7U6g8grvzW/aQcQremX8lJbTanhcD.zGfhOa4JEa7L7p8jY6") // password2
                                .role(Role.USER)
                                .createdAt(OffsetDateTime.now())
                                .updatedAt(OffsetDateTime.now())
                                .isActive(true)
                                .build();

                User user3 = User.builder()
                                .email("charlie@example.com")
                                .username("charlie")
                                .password("$2y$10$q3ihIonAU0.0q0Wvo7rUO.SX5IWV1ofmy.Mf2d0qqqZ6ef0Y3hOfi") // password3
                                .role(Role.ADMIN)
                                .createdAt(OffsetDateTime.now())
                                .updatedAt(OffsetDateTime.now())
                                .isActive(true)
                                .build();

                // Save users only if they don't exist and get the saved instances
                List<User> usersToSave = Arrays.asList(user1, user2, user3);
                for (User user : usersToSave) {
                        if (userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername()).isEmpty()) {
                                user = userRepository.save(user);
                        } else {
                                user = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername()).get();
                        }
                }

                // Create user profiles only for newly saved users
                UserProfile profile1 = UserProfile.builder()
                                .fullName("Alice Smith")
                                .bio("Loves coding and cats.")
                                .location("Cairo")
                                .profilePictureUrl("https://example.com/images/alice.jpg")
                                .user(user1)
                                .build();

                UserProfile profile2 = UserProfile.builder()
                                .fullName("Bob Johnson")
                                .bio("Backend engineer and gamer.")
                                .location("Giza")
                                .profilePictureUrl("https://example.com/images/bob.jpg")
                                .user(user2)
                                .build();

                UserProfile profile3 = UserProfile.builder()
                                .fullName("Charlie King")
                                .bio("Admin of the platform.")
                                .location("Alexandria")
                                .profilePictureUrl("https://example.com/images/charlie.jpg")
                                .user(user3)
                                .build();

                // Save profiles only if they don't exist
                List<UserProfile> profilesToSave = Arrays.asList(profile1, profile2, profile3);
                for (UserProfile profile : profilesToSave) {
                        if (!userProfileRepository.existsByUser(profile.getUser())) {
                                userProfileRepository.save(profile);
                        }
                }

                // Create follow relationships only if they don't exist
                Follower f1 = new Follower(user1, user2); // Alice follows Bob
                Follower f2 = new Follower(user2, user1); // Bob follows Alice
                Follower f3 = new Follower(user2, user3); // Bob follows Charlie

                List<Follower> followersToSave = Arrays.asList(f1, f2, f3);
                for (Follower follower : followersToSave) {
                        if (!followerRepository.existsByFollowerAndFollowing(follower.getFollower(),
                                        follower.getFollowed())) {
                                followerRepository.save(follower);
                        }
                }

                return ResponseEntity.ok("Database seeded successfully.");
        }

        @GetMapping("/users")
        public ResponseEntity<List<User>> getAllUsers() {
                return ResponseEntity.ok(userRepository.findAll());
        }

        @GetMapping("/profiles")
        public ResponseEntity<List<UserProfile>> getAllProfiles() {
                return ResponseEntity.ok(userProfileRepository.findAll());
        }

        @GetMapping("/followers")
        public ResponseEntity<List<Follower>> getAllFollowers() {
                return ResponseEntity.ok(followerRepository.findAll());
        }
}

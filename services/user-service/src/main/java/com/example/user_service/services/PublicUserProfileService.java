package com.example.user_service.services;

import com.example.user_service.clients.AnswerServiceClient;
import com.example.user_service.clients.QuestionServiceClient;
import com.example.user_service.config.RedisCacheConfig;
import com.example.user_service.dto.AnswerDTO;
import com.example.user_service.dto.QuestionDTO;
import com.example.user_service.dto.UserProfileDTO;
import com.example.user_service.models.User;
import com.example.user_service.models.UserProfile;
import com.example.user_service.repositories.UserProfileRepository;
import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicUserProfileService {

    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final QuestionServiceClient questionServiceClient;
    private final AnswerServiceClient answerServiceClient;

    /**
     * This method retrieves the full profile of a user by their username.
     * It fetches the user's profile information, including email, username,
     * full name, bio, location, profile picture URL, followers count,
     * following count, questions, and answers.
     *
     * @param username The username of the user whose profile is to be retrieved.
     * @return UserProfileDTO containing the user's full profile information.
     */
    public UserProfileDTO getFullProfileByUsername(String username) {

        User user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        UserProfile userProfile = userProfileService.getProfileByUserId(user.getId());


        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setFullName(userProfile.getFullName());
        userProfileDTO.setBio(userProfile.getBio());
        userProfileDTO.setLocation(userProfile.getLocation());
        userProfileDTO.setProfilePictureUrl(userProfile.getProfilePictureUrl());
        userProfileDTO.setFollowersCount(userProfile.getFollowerCount());
        userProfileDTO.setFollowingCount(userProfile.getFollowingCount());

        /**
         * Feign client calls to get the questions and the answers
         */
        List<QuestionDTO> questions = questionServiceClient.getUserQuestions(user.getId());
        userProfileDTO.setQuestions(questions);
        userProfileDTO.setQuestionsCount((long) questions.size());

        List<AnswerDTO> answers = answerServiceClient.getUserAnswers(user.getId());
        userProfileDTO.setAnswers(answers);
        userProfileDTO.setAnswersCount((long) answers.size());

        return userProfileDTO;
    }

}

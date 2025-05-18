package com.example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UserProfileDTO class that represents the data transfer object for user profile
 * information.
 * It contains the user's email and username.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    /**
     * The email of the user.
     */
    private String email;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The bio of the user.
     */
    private String bio;

    /**
     * The location of the user.
     */
    private String location;

    /**
     * The URL of the user's profile picture.
     */
    private String profilePictureUrl;

    /**
     * The number of followers the user has.
     */
    private Long followersCount;

    /**
     * The number of users the user is following.
     */
    private Long followingCount;

    /**
     * The number of questions the user has asked.
     */
    private Long questionsCount;

    /**
     * The number of questions the user has asked.
     */
    private List<QuestionDTO> questions;

    /**
     * The number of answers the user has given.
     */
    private Long answersCount;

    /**
     * The number of answers the user has given.
     */
    private List<AnswerDTO> answers;
}

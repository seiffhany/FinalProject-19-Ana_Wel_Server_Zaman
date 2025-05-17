package com.example.user_service.dto;

import com.example.user_service.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * RegisterRequest class represents the request sent to the server during user registration.
 * It contains the user's email, username, password, and role.
 * It also contains user profile related info such as the full_name, bio, profile_picture_url and location.
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "email is required!")
    @Email(message = "email has to be a valid email!")
    private String email;

    @NotBlank(message = "username is required!")
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank(message = "password is required!")
    @Size(min = 8)
    private String password;

    @NotNull(message = "role is required!")
    private Role role;

    private String fullName;

    private String bio;

    private String profilePictureUrl;

    private String location;
}

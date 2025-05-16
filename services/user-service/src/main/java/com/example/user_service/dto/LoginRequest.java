package com.example.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginRequest class that represents the request sent to the server during user
 * login.
 * It contains the user's email and password.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username; // user's username

    @NotBlank(message = "Password is required")
    private String password; // user's password
}

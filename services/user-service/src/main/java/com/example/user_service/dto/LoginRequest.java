package com.example.user_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * LoginRequest class that represents the request sent to the server during user login.
 * It contains the user's email and password.
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username; // user's username

    @NotBlank(message = "Password is required")
    private String password; // user's password
}

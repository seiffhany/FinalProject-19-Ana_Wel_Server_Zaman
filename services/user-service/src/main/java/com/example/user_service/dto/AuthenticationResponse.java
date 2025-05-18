package com.example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthenticationResponse class that represents the response sent to the client
 * after successful authentication.
 * It contains the JWT token that will be used for subsequent requests.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token; // JWT token
}

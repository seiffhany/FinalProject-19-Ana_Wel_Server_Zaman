package com.example.user_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * JwtAuthenticationFilter class that extends OncePerRequestFilter.
 * This filter is used to intercept requests and perform JWT authentication.
 * It ensures that the filter is executed only once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * This method is called to filter the incoming request.
     * It checks for the presence of a JWT token in the Authorization header,
     * validates the token, and sets the authentication in the security context.
     *
     * @param request The incoming HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to continue processing the request.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Get the JWT token from the request
        String jwt = getJwtFromRequest(request);

        // If the token is null, continue the filter chain
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // If the token is empty, continue the filter chain
        if (jwt.isEmpty()) {
            throw new RuntimeException("JWT token is empty");
        }

        // Extract the username from the token
        String username = jwtTokenProvider.extractUsername(jwt);
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("JWT token is invalid as username is null or empty");
        }

        // If the user is not authenticated, load the user details and set the authentication in the security context
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user details using the UserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Check if the token is valid
            if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {

                // Extract roles and user ID from the token just for logging purposes
                List<String> roles = jwtTokenProvider.extractRoles(jwt);
                UUID userId = jwtTokenProvider.extractUserId(jwt);

                // Set the authentication in the security context
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Set the details of the authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

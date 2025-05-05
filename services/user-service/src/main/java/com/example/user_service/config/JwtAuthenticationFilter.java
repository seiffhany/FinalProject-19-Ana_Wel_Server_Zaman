package com.example.user_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

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

        // Log the incoming request URL
        log.info("Incoming request URL: {}", request.getRequestURL());

        // Get the JWT token from the request
        String jwt = getJwtFromRequest(request);

        // Log the extracted JWT token
        log.info("Extracted JWT token: {}", jwt);

        // If the token is null, continue the filter chain
        if (jwt == null) {

            // Log that the JWT token is null
            log.info("JWT token is null, continuing filter chain without authentication");

            filterChain.doFilter(request, response);
            return;
        }

        // If the token is empty, continue the filter chain
        if (jwt.isEmpty()) {

            // Log that the JWT token is empty
            log.error("JWT token is empty");

            throw new RuntimeException("JWT token is empty");
        }

        // check if the token is blacklisted
        if (isTokenBlacklisted(jwt)) {

            // Log that the token is blacklisted
            log.warn("JWT token is blacklisted, rejecting the request");

            // Set the response status to 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Write the response message
            response.getWriter().write("Unauthorized: Token is blacklisted");
            return;
        }

        try {
            // Extract the username from the token
            String username = jwtTokenProvider.extractUsername(jwt);

            // Log the extracted username
            log.info("Extracted username from JWT token: {}", username);

            if (username == null || username.isEmpty()) {

                // Log that the username is null or empty
                log.error("JWT token is invalid as username is null or empty");

                throw new RuntimeException("JWT token is invalid as username is null or empty");
            }

            // If the user is not authenticated, load the user details and set the authentication in the security context
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // Log that the user is not authenticated
                log.info("User is not authenticated, loading user details");

                // Load the user details using the UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Check if the token is valid
                if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {

                    // Extract roles and user ID from the token just for logging purposes
                    List<String> roles = jwtTokenProvider.extractRoles(jwt);
                    UUID userId = jwtTokenProvider.extractUserId(jwt);

                    // Log the extracted roles and user ID
                    log.info("Extracted roles from JWT token: {}", roles);
                    log.info("Extracted user ID from JWT token: {}", userId);

                    // Set the authentication in the security context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Set the details of the authentication object
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Log the details of the authentication object
                    log.info("Authentication details: {}", authentication.getDetails());

                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Log that the authentication has been set in the security context
                    log.info("Authentication set in security context for user: {}", username);
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token has expired");
            return;

        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.UnsupportedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
            return;

        } catch (io.jsonwebtoken.SignatureException e) {
            log.warn("JWT token signature is invalid: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token signature");
            return;

        } catch (Exception e) {
            log.error("Unexpected error during token validation", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Unexpected error during token validation");
            return;
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }


    /**
     * This method checks if the JWT token is blacklisted in Redis.
     *
     * @param jwt The JWT token to check.
     * @return true if the token is blacklisted, false otherwise.
     */
    private boolean isTokenBlacklisted(String jwt) {

        String redisKey = "blacklist:" + jwt;

        // Check if the token is blacklisted in Redis
        Boolean isBlacklisted = redisTemplate.hasKey(redisKey);

        // Log the result of the blacklist check
        if (isBlacklisted != null && isBlacklisted) {
            log.info("Token is blacklisted in Redis");
            return true;
        } else {
            log.info("Token is not blacklisted in Redis");
            return false;
        }
    }

    /**
     * This method extracts the JWT token from the Authorization header of the request.
     *
     * @param request The incoming HTTP request.
     * @return The extracted JWT token or null if not found.
     */
    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        // Log the Authorization header
        log.info("Authorization header: {}", bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            // Log that the JWT token is being extracted from the Authorization header
            log.info("Extracting JWT token from Authorization header");
            return bearerToken.substring(7);
        }

        // Log that the JWT token is not found in the Authorization header
        log.warn("JWT token not found in Authorization header");
        return null;
    }
}

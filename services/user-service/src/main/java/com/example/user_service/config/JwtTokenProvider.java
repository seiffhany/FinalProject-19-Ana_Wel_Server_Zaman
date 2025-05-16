package com.example.user_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

/**
 * JwtTokenProvider class that provides methods for generating and validating JWT tokens.
 * This class is responsible for creating JWT tokens and extracting information from them.
 */
public class JwtTokenProvider {
    private static volatile JwtTokenProvider instance;
    private final String secretKey; // secret key for signing the JWT token
    private final long expirationTime; // expiration time for the JWT token in milliseconds

    private JwtTokenProvider(String secretKey, long expirationTime) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
    }

    /**
     * This method returns a singleton instance of JwtTokenProvider.
     * It ensures that only one instance of the class is created and used throughout the application.
     *
     * @param secretKey The secret key for signing the JWT token.
     * @param expirationTime The expiration time for the JWT token in milliseconds.
     * @return The singleton instance of JwtTokenProvider.
     */
    public static JwtTokenProvider getInstance(String secretKey, long expirationTime) {
        if (instance == null) {
            synchronized (JwtTokenProvider.class) {
                if (instance == null) {

                    if (secretKey == null || secretKey.isEmpty()) {
                        throw new IllegalStateException("Secret key must not be null or empty");
                    }
                    if (expirationTime <= 0) {
                        throw new IllegalStateException("Expiration time must be greater than 0");
                    }
                    instance = new JwtTokenProvider(secretKey, expirationTime);
                }
            }
        }
        return instance;
    }

    /**
     * This method extracts all claims from the JWT token.
     * It uses the Jwts parser to parse the token and extract the claims.
     * The signing key is set to validate the token.
     *
     * @param token The JWT token from which to extract claims.
     * @return Claims object containing all claims extracted from the token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * This method extracts a specific claim from the JWT token.
     * It uses a claims resolver function to extract the desired claim.
     *
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolver A function that takes Claims as input and returns the desired claim.
     * @param <T> The type of the claim to be extracted.
     * @return The extracted claim of type T.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This method extracts the username (subject) from the JWT token.
     * It uses the extractClaim method to get the subject claim from the token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This method extracts the user ID from the JWT token.
     * It uses the extractClaim method to get the userId claim from the token.
     *
     * @param token The JWT token from which to extract the user ID.
     * @return The user ID extracted from the token as a UUID.
     */
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("userId", String.class)));
    }

    /**
     * This method extracts the roles from the JWT token.
     * It uses the extractClaim method to get the roles claim from the token.
     *
     * @param Roles The JWT token from which to extract the roles.
     * @return A list of roles extracted from the token.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String Roles) {
        return extractClaim(Roles, claims -> (List<String>) claims.get("roles", List.class));
    }

    /**
     * This method extracts the email from the JWT token.
     * It uses the extractClaim method to get the userEmail claim from the token.
     *
     * @param token The JWT token from which to extract the email.
     * @return The email extracted from the token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("userEmail", String.class));
    }

    /**
     * This method extracts the expiration date from the JWT token.
     * It uses the extractClaim method to get the expiration claim from the token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date extracted from the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * This method generates a JWT token using the provided extra claims and user details.
     * It sets the subject (username), issued date, expiration date, and signs the token with the signing key.
     *
     * @param extraClaims A map of extra claims to be included in the token.
     * @param userDetails The user details object containing the username and other information.
     * @param userId The user ID to be included in the token.
     * @return The generated JWT token as a string.
     */
    public String generateToken (
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            UUID userId,
            String userEmail
    ) {

        if (userDetails == null) {
            throw new RuntimeException("UserDetails must not be null");
        }

        if (userId == null) {
            throw new RuntimeException("UserId must not be null");
        }

        extraClaims.put("userId", userId.toString());
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        extraClaims.put("roles", roles);
        extraClaims.put("userEmail", userEmail);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * This method generates a JWT token using the provided user details.
     * It sets the subject (username), issued date, expiration date, and signs the token with the signing key.
     *
     * @param userDetails The user details object containing the username and other information.
     * @param userId The user ID to be included in the token.
     * @return The generated JWT token as a string.
     */
    public String generateToken(UserDetails userDetails, UUID userId, String userEmail) {
        if (userDetails == null) {
            throw new RuntimeException("UserDetails must not be null");
        }

        if (userId == null) {
            throw new RuntimeException("UserId must not be null");
        }

        if (userEmail == null || userEmail.isEmpty()) {
            throw new RuntimeException("UserEmail must not be null or empty");
        }

        return generateToken(new HashMap<>(), userDetails, userId, userEmail);
    }

    /**
     * This method checks if the JWT token is expired.
     * It compares the current date with the expiration date of the token.
     *
     * @param token The JWT token to be checked.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * This method checks if the JWT token is valid.
     * It compares the username in the token with the provided user details and checks if the token is expired.
     *
     * @param token The JWT token to be validated.
     * @param userDetails The user details object containing the username and other information.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token must not be null or empty");
        }
        if (userDetails == null) {
            throw new RuntimeException("UserDetails must not be null");
        }
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * This method retrieves the signing key used to sign the JWT token.
     * It decodes the secret key from Base64 and creates a Key object.
     *
     * @return The signing key as a Key object.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

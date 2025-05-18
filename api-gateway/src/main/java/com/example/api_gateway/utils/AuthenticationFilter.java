package com.example.api_gateway.utils;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.api_gateway.config.PublicRoutesConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PublicRoutesConfig publicRoutesConfig;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        if (publicRoutesConfig.getPaths().stream().anyMatch(path::contains)) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            if (isTokenBlacklisted(token)) {
                log.info("Token is blacklisted");
                return onError(exchange, "Token is blacklisted", HttpStatus.UNAUTHORIZED);
            }

            if (!jwtTokenProvider.isTokenValid(token)) {
                return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            // Optionally extract info and forward it to downstream services
            String username = jwtTokenProvider.extractUsername(token);
            UUID userId = jwtTokenProvider.extractUserId(token);
            var roles = jwtTokenProvider.extractRoles(token);
            String userEmail = jwtTokenProvider.extractEmail(token);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("userId", userId.toString())
                    .header("username", username)
                    .header("roles", String.join(",", roles))
                    .header("userEmail", userEmail)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            return onError(exchange, "Token processing failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
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
}

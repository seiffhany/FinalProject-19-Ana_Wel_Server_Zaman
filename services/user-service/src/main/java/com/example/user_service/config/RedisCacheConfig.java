package com.example.user_service.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.user_service.models.UserProfile;

@Configuration
@EnableCaching
public class RedisCacheConfig {

  public static final String USER_PROFILE_CACHE = "user_profile_cache";

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    // Default cache configuration
    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(24)) // TTL set to 24 hours
        .disableCachingNullValues() // Disable caching null values
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new Jackson2JsonRedisSerializer<>(Object.class)));

    // Custom configurations for different entities
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    // Cache configuration for UserProfile
    cacheConfigurations.put(USER_PROFILE_CACHE,
        defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new Jackson2JsonRedisSerializer<>(UserProfile.class))));

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(defaultConfig) // Default settings
        .withInitialCacheConfigurations(cacheConfigurations) // Custom per-cache configurations
        .build();
  }
}
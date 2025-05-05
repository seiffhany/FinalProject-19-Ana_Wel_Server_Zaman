package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration class that configures the RedisTemplate bean.
 * This class is responsible for setting up the Redis connection factory and serializers.
 */
@Configuration
public class RedisConfig {

    /**
     * This method creates a RedisTemplate bean.
     * It is used to interact with Redis data store.
     * The key serializer is set to StringRedisSerializer and the value serializer is set to GenericJackson2JsonRedisSerializer.
     *
     * @param redisConnectionFactory The RedisConnectionFactory object used to create the Redis connection.
     * @return RedisTemplate instance
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}

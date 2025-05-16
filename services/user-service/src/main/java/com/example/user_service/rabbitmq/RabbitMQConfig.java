package com.example.user_service.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notificationQueue";
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notificationRoutingKey";
    public static final String USER_ROUTING_KEY = "userRoutingKey";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(USER_ROUTING_KEY);
    }
}
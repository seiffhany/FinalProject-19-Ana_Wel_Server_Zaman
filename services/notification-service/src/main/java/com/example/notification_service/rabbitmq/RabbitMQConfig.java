package com.example.notification_service.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notificationQueue";
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String ROUTING_KEY = "notificationRoutingKey";

    /**
     * The queue is used to store messages related to notifications.
     * Auto-delete is set to true so the queue is deleted when the application
     * stops.
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    /**
     * The exchange is used to route messages to the appropriate queue based on the
     * routing key.
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * The binding connects the queue to the exchange using the specified routing
     * key.
     */
    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(ROUTING_KEY);
    }

    /**
     * Configure the message converter to use JSON instead of Java serialization.
     * This avoids deserialization security issues and is more efficient.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
package com.example.question_service.rabbitmq;

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
    public static final String QUESTION_ROUTING_KEY = "questionRoutingKey";

    /**
     * The queue is used to store messages related to notifications.
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME);
    }

    /**
     * The exchange is used to route messages to the appropriate queue based on the routing key.
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * The binding connects the queue to the exchange using the specified routing key.
     */
    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(QUESTION_ROUTING_KEY);
    }

    //Configuration for communication with answer service

    public static final String QUESTION_QUEUE_NAME = "questionQueue";
    public static final String QUESTION_EXCHANGE_NAME = "questionExchange";
    public static final String ANSWER_ROUTING_KEY = "answerRoutingKey";

    @Bean
    public Queue questionQueue() {
        return new Queue(QUESTION_QUEUE_NAME);
    }
    @Bean
    public TopicExchange questionExchange() {
        return new TopicExchange(QUESTION_EXCHANGE_NAME);
    }
    @Bean
    public Binding questionBinding(Queue questionQueue, TopicExchange questionExchange) {
        return BindingBuilder
                .bind(questionQueue)
                .to(questionExchange)
                .with(QUESTION_ROUTING_KEY);
    }



}
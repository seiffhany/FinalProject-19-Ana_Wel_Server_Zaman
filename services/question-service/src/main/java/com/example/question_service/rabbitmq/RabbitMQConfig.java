package com.example.question_service.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notificationQueue";
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notificationRoutingKey";

    /**
     * The queue is used to store messages related to notifications.
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME);
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
                .with(QUESTION_ROUTING_KEY);
    }

    // Configuration for communication with answer service

    public static final String QUESTION_QUEUE_NAME = "questionQueue";
    public static final String QUESTION_EXCHANGE_NAME = "questionExchange";
    public static final String QUESTION_ROUTING_KEY = "questionRoutingKey";

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

    public static final String QUESTION_DELETED_QUEUE_NAME = "question_deleted_queue";
    public static final String QUESTION_DELETED_ROUTING_KEY = "question_deleted_routing_key";
    public static final String QUESTION_DELETED_EXCHANGE_NAME = "question_deleted_exchange";

    @Bean
    public Queue questionDeletedQueue() {
        return new Queue(QUESTION_DELETED_QUEUE_NAME);
    }

    @Bean
    public TopicExchange questionDeletedExchange() {
        return new TopicExchange(QUESTION_DELETED_EXCHANGE_NAME);
    }

    @Bean
    public Binding questionDeletedBinding(Queue questionDeletedQueue, TopicExchange questionDeletedExchange) {
        return BindingBuilder
                .bind(questionDeletedQueue)
                .to(questionDeletedExchange)
                .with(QUESTION_DELETED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
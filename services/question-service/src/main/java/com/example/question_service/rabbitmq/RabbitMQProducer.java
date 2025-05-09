package com.example.question_service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUpvoteNotification(String questionContent, String userName) {
        String message = "Question " + questionContent + " was upvoted by user " + userName;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendDownvoteNotification(String questionContent, String userName) {
        String message = "Question " + questionContent + " was downvoted by user " + userName;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendViewCountNotification(String questionContent, int viewCount) {
        String message = "Question " + questionContent + " reached " + viewCount + " views";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendQuestionCreatedNotification(String questionContent, String userName) {
        String message = "New question " + questionContent + " created by user " + userName;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendTrendingQuestionNotification(String questionContent) {
        String message = "Question " + questionContent + " is now trending";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendTaggedQuestionNotification(String questionContent, String tag, String userName) {
        String message = "Question " + questionContent + " tagged with " + tag + " might interest user " + userName;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
}
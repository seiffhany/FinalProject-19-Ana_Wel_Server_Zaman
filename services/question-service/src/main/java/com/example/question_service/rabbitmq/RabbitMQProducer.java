package com.example.question_service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUpvoteNotification(String notificationReceiverId, String questionContent, String upvoterUserName) {
        String[] message = {"Upvote", notificationReceiverId, questionContent, upvoterUserName};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendDownvoteNotification(String notificationReceiverId, String questionContent, String userName) {
        String[] message = {"Downvote", notificationReceiverId, questionContent, userName};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendViewCountNotification(String questionCreatorUserId, String questionContent, int viewCount) {
        String[] message = {"Question View Count",questionCreatorUserId, questionContent, viewCount + ""};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

    public void sendQuestionCreatedNotification(String[] recipientUsersIds, String questionContent, String userName) {
        String Ids="";
        for (String recipientUserId : recipientUsersIds) {
            Ids += recipientUserId + ",";
        }
        String[] message = {"Question New", Ids, questionContent, userName};
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }

//    public void sendTrendingQuestionNotification(String questionContent) {
//        String message = "Question " + questionContent + " is now trending";
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
//    }

//    public void sendTaggedQuestionNotification(String questionContent, String tag, String userName) {
//        String message = "Question " + questionContent + " tagged with " + tag + " might interest user " + userName;
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
//    }
    public void sendQuestionDeletedToAnswerService(String questionId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUESTION_EXCHANGE_NAME, RabbitMQConfig.ANSWER_ROUTING_KEY, questionId);
    }
}
package com.example.notification_service.rabbitmq;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import com.example.notification_service.services.NotificationService;
import com.example.notification_service.utils.NotificationMessage;

@Service
public class RabbitMQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(NotificationMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Headers Map<String, Object> headers) {
        logger.info("Received message: {}, deliveryTag: {}, headers: {}", message, deliveryTag, headers);
        try {
            if (message == null) {
                logger.error("Invalid message: Message is null");
                throw new AmqpRejectAndDontRequeueException("Message cannot be null");
            }
            if (message.getRecipientEmails() == null || message.getRecipientEmails().isEmpty()) {
                logger.error("Invalid message: No recipients specified");
                throw new AmqpRejectAndDontRequeueException("Message must have at least one recipient");
            }
            if (message.getCategory() == null || message.getType() == null) {
                logger.error("Invalid message: Category or Type is null");
                throw new AmqpRejectAndDontRequeueException("Message must have both category and type");
            }
            logger.info("Processing message: category={}, type={}, recipients={}", message.getCategory(),
                    message.getType(), message.getRecipientEmails());

            // SENDING THE NOTIFICATION
            notificationService.sendNotification(message);

            logger.info("Successfully processed message for recipients: {}", message.getRecipientEmails());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid message format: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException(
                    "Failed to process message due to invalid format: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Failed to process message: " + e.getMessage(), e);
        }
    }
}
package com.eager.questioncloud.application.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageType messageType, Object message) {
        rabbitTemplate.convertAndSend(messageType.getQueueName(), message);
    }
}

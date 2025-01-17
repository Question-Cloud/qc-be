package com.eager.questioncloud.application.message;

import com.eager.questioncloud.application.config.RabbitConfig;
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

    public void sendDelayMessage(int delay, MessageType messageType, Object message) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.DELAY_EXCHANGE,
            messageType.getQueueName(),
            message,
            m -> {
                m.getMessageProperties().setHeader("target", "delay");
                m.getMessageProperties().setExpiration(String.valueOf(delay));
                return m;
            });
    }
}

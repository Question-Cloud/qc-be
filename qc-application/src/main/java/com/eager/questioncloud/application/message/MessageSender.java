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

    public void sendDelayMessage(MessageType messageType, Object message, int failCount) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.DELAY_EXCHANGE,
            messageType.getQueueName(),
            message,
            m -> {
                m.getMessageProperties().setHeader("delay", "delay");
                m.getMessageProperties().setExpiration(selectDelay(failCount));
                return m;
            });
    }

    private String selectDelay(int failCount) {
        switch (failCount) {
            default:
                return "5000";
        }
    }
}

package com.eager.questioncloud.application.message

import com.eager.questioncloud.application.config.RabbitConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class MessageSender(
    private val rabbitTemplate: RabbitTemplate
) {
    fun sendMessage(messageType: MessageType, message: Any) {
        rabbitTemplate.convertAndSend(messageType.queueName, message)
    }

    fun sendDelayMessage(messageType: MessageType, message: Any, failCount: Int) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.DELAY_EXCHANGE, messageType.queueName, message
        ) { m ->
            m.apply { messageProperties.expiration = selectDelay(failCount) }
        }
    }

    private fun selectDelay(failCount: Int): String {
        when (failCount) {
            else -> return "10000"
        }
    }
}

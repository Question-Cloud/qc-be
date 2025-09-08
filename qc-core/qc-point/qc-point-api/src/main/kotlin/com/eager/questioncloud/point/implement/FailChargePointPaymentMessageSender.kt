package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.common.message.MessageSender
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentMessageSender(
    private val messageSender: MessageSender,
) {
    private val logger = LoggerFactory.getLogger("error-publish-fail-charge-point-event")
    private val queueName = "fail-charge-point-payment.fifo"
    
    fun publishMessage(message: FailChargePointPaymentMessagePayload) {
        runCatching {
            messageSender.sendMessage(message, queueName, message.orderId)
        }.onFailure { logger.warn(message.orderId) }
    }
}
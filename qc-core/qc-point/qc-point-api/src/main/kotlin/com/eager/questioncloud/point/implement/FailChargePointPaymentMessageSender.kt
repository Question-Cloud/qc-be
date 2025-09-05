package com.eager.questioncloud.point.implement

import com.eager.questioncloud.event.model.FailChargePointPaymentMessage
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentMessageSender(
    private val sqsTemplate: SqsTemplate,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger("error-publish-fail-charge-point-event")
    private val queueName = "fail-charge-point-payment.fifo"
    
    fun publishMessage(message: FailChargePointPaymentMessage) {
        runCatching {
            sqsTemplate.send { to ->
                to.queue(queueName)
                    .messageGroupId(message.orderId)
                    .messageDeduplicationId(message.orderId)
                    .payload(objectMapper.writeValueAsString(message))
            }
        }.onFailure { logger.warn(message.orderId) }
    }
}
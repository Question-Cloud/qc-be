package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentEventProcessor(
    private val sqsTemplate: SqsTemplate,
) {
    private val logger = LoggerFactory.getLogger("error-publish-fail-charge-point-event")

    fun publishEvent(failChargePointPaymentEvent: FailChargePointPaymentEvent) {
        try {
            sqsTemplate.send { to ->
                to.queue("fail-charge-point-payment.fifo").payload(failChargePointPaymentEvent.toJson())
            }
        } catch (e: Exception) {
            logger.warn(failChargePointPaymentEvent.orderId)
        }
    }
}
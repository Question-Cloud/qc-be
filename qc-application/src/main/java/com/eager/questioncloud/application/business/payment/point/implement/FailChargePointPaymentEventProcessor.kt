package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.business.payment.point.event.FailChargePointPaymentEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sns.SnsClient

@Component
class FailChargePointPaymentEventProcessor(
    private val snsClient: SnsClient,
) {
    private val logger = LoggerFactory.getLogger("error-publish-fail-charge-point-event")

    fun publishEvent(failChargePointPaymentEvent: FailChargePointPaymentEvent) {
        try {
            snsClient.publish(failChargePointPaymentEvent.toRequest())
        } catch (e: Exception) {
            logger.warn(failChargePointPaymentEvent.paymentId)
        }
    }
}
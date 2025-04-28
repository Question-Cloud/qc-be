package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.application.event.AbstractEventProcessor
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentEventProcessor(
    private val sqsTemplate: SqsTemplate,
) : AbstractEventProcessor<FailChargePointPaymentEvent>() {
    private val logger = LoggerFactory.getLogger("error-publish-fail-charge-point-event")

    override fun publishEvent(event: FailChargePointPaymentEvent) {
        runCatching {
            sqsTemplate.send { to -> to.queue("fail-charge-point-payment.fifo").payload(event.toJson()) }
        }.onFailure { logger.warn(event.orderId) }
    }

    override suspend fun republishScheduled() {}
    
    override fun saveEventLog(event: FailChargePointPaymentEvent) {}

    override fun getUnpublishedEvents(): List<FailChargePointPaymentEvent> {
        return emptyList()
    }

    override suspend fun republish(events: List<FailChargePointPaymentEvent>) {}
}
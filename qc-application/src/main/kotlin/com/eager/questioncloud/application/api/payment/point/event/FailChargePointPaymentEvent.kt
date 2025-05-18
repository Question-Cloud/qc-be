package com.eager.questioncloud.application.api.payment.point.event

import com.eager.questioncloud.application.event.SQSEvent
import software.amazon.awssdk.services.sns.model.PublishRequest

class FailChargePointPaymentEvent(
    override val eventId: String,
    val orderId: String,
) : SQSEvent {
    companion object {
        fun create(orderId: String): FailChargePointPaymentEvent {
            return FailChargePointPaymentEvent(orderId, orderId)
        }
    }

    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:fail-charge-point-payment.fifo")
            .messageGroupId(orderId)
            .messageDeduplicationId(orderId)
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
}
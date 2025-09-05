package com.eager.questioncloud.event.model

import com.eager.questioncloud.event.SNSEvent
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest

class FailChargePointPaymentEvent(
    override val eventId: String,
    val orderId: String,
) : SNSEvent {
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
            .message(SNSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun toBatchRequestEntry(): PublishBatchRequestEntry {
        TODO("Not yet implemented")
    }
    
    override fun getTopicArn(): String {
        TODO("Not yet implemented")
    }
}
package com.eager.questioncloud.event.model

import com.eager.questioncloud.event.SQSEvent
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import software.amazon.awssdk.services.sns.model.PublishRequest

class QuestionPaymentEvent(
    override val eventId: String,
    val data: QuestionPaymentEventData
) : SQSEvent {
    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:question-payment-sns.fifo")
            .messageGroupId(data.orderId)
            .messageDeduplicationId(data.orderId)
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }

    companion object {
        fun create(data: QuestionPaymentEventData): QuestionPaymentEvent {
            return QuestionPaymentEvent(data.orderId, data)
        }
    }
}

class QuestionPaymentEventData(
    val orderId: String,
    val buyerUserId: Long,
    val questionIds: List<Long>,
    val amount: Int,
    val questionPaymentCoupon: QuestionPaymentCoupon?,
)

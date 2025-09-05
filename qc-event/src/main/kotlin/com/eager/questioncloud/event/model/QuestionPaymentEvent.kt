package com.eager.questioncloud.event.model

import com.eager.questioncloud.event.SQSEvent
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest

class QuestionPaymentEvent(
    override val eventId: String,
    val data: QuestionPaymentEventData
) : SQSEvent {
    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .messageGroupId(data.orderId)
            .messageDeduplicationId(data.orderId)
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun toBatchRequestEntry(): PublishBatchRequestEntry {
        return PublishBatchRequestEntry.builder()
            .id(eventId)
            .messageGroupId(data.orderId)
            .messageDeduplicationId(data.orderId)
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun getTopicArn(): String {
        return topicArn
    }
    
    companion object {
        private const val topicArn = "arn:aws:sns:ap-northeast-2:503561444273:question-payment-sns.fifo"
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
    val questionPaymentCoupon: QuestionPaymentEventCouponData?,
)

class QuestionPaymentEventCouponData(
    val userCouponId: Long,
    val couponId: Long,
    val title: String,
    val couponType: String,
    val value: Int,
)
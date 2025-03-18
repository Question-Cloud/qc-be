package com.eager.questioncloud.application.api.payment.question.event

import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import software.amazon.awssdk.services.sns.model.PublishRequest

class QuestionPaymentEvent(
    override val eventId: String,
    val questionPayment: QuestionPayment
) : SQSEvent {
    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:question-payment-sns.fifo")
            .messageGroupId(questionPayment.order.orderId)
            .messageDeduplicationId(questionPayment.order.orderId)
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }

    companion object {
        fun create(questionPayment: QuestionPayment): QuestionPaymentEvent {
            return QuestionPaymentEvent(questionPayment.order.orderId, questionPayment)
        }
    }
}

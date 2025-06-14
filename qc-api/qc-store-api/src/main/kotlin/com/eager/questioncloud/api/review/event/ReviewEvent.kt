package com.eager.questioncloud.api.review.event

import com.eager.questioncloud.event.SQSEvent
import io.hypersistence.tsid.TSID
import software.amazon.awssdk.services.sns.model.PublishRequest

class ReviewEvent(
    override val eventId: String,
    val questionId: Long,
    val varianceRate: Int,
    val reviewEventType: ReviewEventType
) : SQSEvent {
    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:question-review.fifo")
            .messageDeduplicationId(eventId)
            .messageGroupId(questionId.toString())
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }

    companion object {
        fun create(questionId: Long, varianceRate: Int, reviewEventType: ReviewEventType): ReviewEvent {
            return ReviewEvent(TSID.Factory.getTsid().toString(), questionId, varianceRate, reviewEventType)
        }
    }
}
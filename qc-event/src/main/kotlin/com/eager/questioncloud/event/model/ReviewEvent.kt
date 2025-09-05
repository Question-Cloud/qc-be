package com.eager.questioncloud.event.model

import com.eager.questioncloud.event.SNSEvent
import io.hypersistence.tsid.TSID
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest

class ReviewEvent(
    override val eventId: String,
    val questionId: Long,
    val varianceRate: Int,
    val reviewEventType: ReviewEventType
) : SNSEvent {
    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn(topicArn)
            .messageDeduplicationId(eventId)
            .messageGroupId(questionId.toString())
            .message(SNSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun toBatchRequestEntry(): PublishBatchRequestEntry {
        return PublishBatchRequestEntry.builder()
            .id(eventId)
            .messageDeduplicationId(eventId)
            .messageGroupId(questionId.toString())
            .message(SNSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun getTopicArn(): String {
        return topicArn
    }
    
    companion object {
        private const val topicArn = "arn:aws:sns:ap-northeast-2:503561444273:question-review.fifo"
        fun create(questionId: Long, varianceRate: Int, reviewEventType: ReviewEventType): ReviewEvent {
            return ReviewEvent(TSID.Factory.getTsid().toString(), questionId, varianceRate, reviewEventType)
        }
    }
}
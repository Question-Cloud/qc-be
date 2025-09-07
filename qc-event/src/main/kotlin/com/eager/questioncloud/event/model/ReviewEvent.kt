package com.eager.questioncloud.event.model

import com.eager.questioncloud.event.SNSEvent
import com.fasterxml.jackson.databind.ObjectMapper
import io.hypersistence.tsid.TSID
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest

class ReviewEvent(
    override val eventId: String,
    val questionId: Long,
    val varianceRate: Int,
    val reviewEventType: ReviewEventType
) : SNSEvent {
    override fun toRequest(objectMapper: ObjectMapper): PublishRequest {
        return PublishRequest.builder()
            .topicArn(topicArn)
            .messageDeduplicationId(eventId)
            .messageGroupId(questionId.toString())
            .message(objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun toBatchRequestEntry(objectMapper: ObjectMapper): PublishBatchRequestEntry {
        return PublishBatchRequestEntry.builder()
            .id(eventId)
            .messageDeduplicationId(eventId)
            .messageGroupId(questionId.toString())
            .message(objectMapper.writeValueAsString(this))
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
package com.eager.questioncloud.event.implement

import com.eager.questioncloud.event.SQSEvent
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest
import java.util.*

class TestEvent(
    override val eventId: String,
    val message: String,
) : SQSEvent {
    override fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:test.fifo")
            .messageGroupId(UUID.randomUUID().toString())
            .messageDeduplicationId(UUID.randomUUID().toString())
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun toBatchRequestEntry(): PublishBatchRequestEntry {
        return PublishBatchRequestEntry.builder()
            .id(eventId)
            .messageDeduplicationId(eventId)
            .messageGroupId(eventId)
            .message(SQSEvent.objectMapper.writeValueAsString(this))
            .build()
    }
    
    override fun getTopicArn(): String {
        return "arn:aws:sns:ap-northeast-2:503561444273:test.fifo"
    }
}
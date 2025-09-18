package com.eager.questioncloud.event.model

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.common.event.EventType
import com.eager.questioncloud.event.TopicArn
import com.fasterxml.jackson.databind.ObjectMapper
import software.amazon.awssdk.services.sns.model.PublishRequest
import java.time.LocalDateTime

class EventTicket(
    val eventId: String,
    var isPublish: Boolean,
    val eventType: EventType,
    val topicArn: String,
    val payload: String,
    val createdAt: LocalDateTime,
    var publishedAt: LocalDateTime?
) {
    companion object {
        fun create(event: Event, objectMapper: ObjectMapper): EventTicket {
            return EventTicket(
                event.eventId,
                false,
                event.eventType,
                TopicArn.valueOf(event.eventType.name).topicArn,
                objectMapper.writeValueAsString(event),
                LocalDateTime.now(),
                null
            )
        }
    }
    
    fun toPublishRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn(topicArn)
            .messageGroupId(eventId)
            .messageDeduplicationId(eventId)
            .message(payload)
            .build()
    }
}
package com.eager.questioncloud.event.model

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.event.Topic
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDateTime

class EventTicket(
    val eventId: String,
    var isPublish: Boolean,
    val eventType: String,
    val payload: String,
    val createdAt: LocalDateTime,
    var publishedAt: LocalDateTime?
) {
    companion object {
        fun create(event: Event, objectMapper: ObjectMapper): EventTicket {
            return EventTicket(
                event.eventId,
                false,
                Topic.valueOf(event.eventType.name).topicArn,
                objectMapper.writeValueAsString(event.payload),
                LocalDateTime.now(),
                null
            )
        }
    }
}
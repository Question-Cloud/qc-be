package com.eager.questioncloud.event.model

import com.eager.questioncloud.common.event.Event
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
        fun create(event: Event, objectMapper: ObjectMapper): com.eager.questioncloud.event.model.EventTicket {
            return com.eager.questioncloud.event.model.EventTicket(
                event.eventId,
                false,
                com.eager.questioncloud.event.Topic.valueOf(event.eventType.name).topicArn,
                objectMapper.writeValueAsString(event),
                LocalDateTime.now(),
                null
            )
        }
    }
}
package com.eager.questioncloud.event.model

import com.eager.questioncloud.event.SNSEvent
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
        fun <T : SNSEvent> create(event: T, objectMapper: ObjectMapper): EventTicket {
            return EventTicket(event.eventId, false, event.getTopicArn(), event.toJson(objectMapper), LocalDateTime.now(), null)
        }
    }
}
package com.eager.questioncloud.core.domain.event.model

import java.time.LocalDateTime

class EventProcessLog(
    val eventId: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(eventId: String): EventProcessLog {
            return EventProcessLog(eventId, LocalDateTime.now())
        }
    }
}
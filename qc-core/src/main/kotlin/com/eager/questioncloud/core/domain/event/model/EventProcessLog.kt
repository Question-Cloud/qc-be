package com.eager.questioncloud.core.domain.event.model

import java.time.LocalDateTime

class EventProcessLog(
    val idempotentKey: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(idempotentKey: String): EventProcessLog {
            return EventProcessLog(idempotentKey, LocalDateTime.now())
        }
    }
}
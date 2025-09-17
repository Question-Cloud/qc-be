package com.eager.questioncloud.event.model

import java.time.LocalDateTime

class EventProcessLog(
    val idempotentKey: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(idempotentKey: String): com.eager.questioncloud.event.model.EventProcessLog {
            return com.eager.questioncloud.event.model.EventProcessLog(idempotentKey, LocalDateTime.now())
        }
    }
}
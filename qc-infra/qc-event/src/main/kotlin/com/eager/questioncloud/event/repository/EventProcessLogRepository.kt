package com.eager.questioncloud.event.repository

import com.eager.questioncloud.event.model.EventProcessLog

interface EventProcessLogRepository {
    fun existsByIdempotentKey(idempotentKey: String): Boolean
    fun save(eventProcessLog: EventProcessLog)
}
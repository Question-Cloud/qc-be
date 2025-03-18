package com.eager.questioncloud.core.domain.event.infrastructure.repository

import com.eager.questioncloud.core.domain.event.model.EventProcessLog

interface EventProcessLogRepository {
    fun existsByIdempotentKey(idempotentKey: String): Boolean
    fun save(eventProcessLog: EventProcessLog)
}
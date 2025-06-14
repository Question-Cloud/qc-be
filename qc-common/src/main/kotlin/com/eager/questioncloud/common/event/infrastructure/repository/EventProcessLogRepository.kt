package com.eager.questioncloud.common.event.infrastructure.repository

import com.eager.questioncloud.common.event.model.EventProcessLog

interface EventProcessLogRepository {
    fun existsByIdempotentKey(idempotentKey: String): Boolean
    fun save(eventProcessLog: EventProcessLog)
}
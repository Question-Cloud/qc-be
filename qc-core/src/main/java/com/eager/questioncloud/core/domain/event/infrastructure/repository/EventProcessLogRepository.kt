package com.eager.questioncloud.core.domain.event.infrastructure.repository

interface EventProcessLogRepository {
    fun existsByEventId(eventId: String): Boolean
}
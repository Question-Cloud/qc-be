package com.eager.questioncloud.core.domain.event.implement

import com.eager.questioncloud.core.domain.event.infrastructure.repository.EventProcessLogRepository
import org.springframework.stereotype.Component

@Component
class EventProcessLogReader(
    private val eventProcessLogRepository: EventProcessLogRepository
) {
    fun isProcessedEvent(eventId: String): Boolean {
        return eventProcessLogRepository.existsByEventId(eventId)
    }
}
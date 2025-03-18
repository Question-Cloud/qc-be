package com.eager.questioncloud.core.domain.event.infrastructure.repository

import org.springframework.stereotype.Repository

@Repository
class EventProcessLogRepositoryImpl(
    private val eventProcessLogJpaRepository: EventProcessLogJpaRepository
) : EventProcessLogRepository {
    override fun existsByEventId(eventId: String): Boolean {
        return eventProcessLogJpaRepository.existsByEventId(eventId)
    }
}
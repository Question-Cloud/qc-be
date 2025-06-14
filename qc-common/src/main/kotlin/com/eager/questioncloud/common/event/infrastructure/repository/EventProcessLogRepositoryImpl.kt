package com.eager.questioncloud.common.event.infrastructure.repository

import com.eager.questioncloud.common.event.infrastructure.entity.EventProcessLogEntity
import com.eager.questioncloud.common.event.model.EventProcessLog
import org.springframework.stereotype.Repository

@Repository
class EventProcessLogRepositoryImpl(
    private val eventProcessLogJpaRepository: EventProcessLogJpaRepository
) : EventProcessLogRepository {
    override fun existsByIdempotentKey(idempotentKey: String): Boolean {
        return eventProcessLogJpaRepository.existsByIdempotentKey(idempotentKey)
    }

    override fun save(eventProcessLog: EventProcessLog) {
        eventProcessLogJpaRepository.save(EventProcessLogEntity.createNewEntity(eventProcessLog))
    }
}
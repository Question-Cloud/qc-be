package com.eager.questioncloud.core.domain.event.infrastructure.repository

import com.eager.questioncloud.core.domain.event.infrastructure.entity.EventProcessLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventProcessLogJpaRepository : JpaRepository<EventProcessLogEntity, String> {
    fun existsByIdempotentKey(idempotentKey: String): Boolean
}
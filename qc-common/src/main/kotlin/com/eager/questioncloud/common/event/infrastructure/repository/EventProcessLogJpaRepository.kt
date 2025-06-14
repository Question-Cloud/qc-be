package com.eager.questioncloud.common.event.infrastructure.repository

import com.eager.questioncloud.common.event.infrastructure.entity.EventProcessLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventProcessLogJpaRepository : JpaRepository<EventProcessLogEntity, String> {
    fun existsByIdempotentKey(idempotentKey: String): Boolean
}
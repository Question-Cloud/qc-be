package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorStatisticsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CreatorStatisticsJpaRepository : JpaRepository<CreatorStatisticsEntity, Long> {
    fun findByCreatorId(creatorId: Long): Optional<CreatorStatisticsEntity>

    fun findByCreatorIdIn(creatorIds: List<Long>): List<CreatorStatisticsEntity>
}

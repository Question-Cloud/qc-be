package com.eager.questioncloud.creator.infrastructure.repository

import com.eager.questioncloud.creator.infrastructure.entity.CreatorStatisticsEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CreatorStatisticsJpaRepository : JpaRepository<CreatorStatisticsEntity, Long> {
    fun findByCreatorId(creatorId: Long): Optional<CreatorStatisticsEntity>

    fun findByCreatorIdIn(creatorIds: List<Long>): List<CreatorStatisticsEntity>

    @Query("SELECT cs from CreatorStatisticsEntity cs where cs.creatorId = :creatorId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun getForUpdate(creatorId: Long): CreatorStatisticsEntity?
}

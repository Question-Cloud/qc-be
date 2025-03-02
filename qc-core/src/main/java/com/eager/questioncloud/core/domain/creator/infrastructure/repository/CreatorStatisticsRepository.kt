package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics

interface CreatorStatisticsRepository {
    fun save(creatorStatistics: CreatorStatistics)

    fun saveAll(creatorStatistics: List<CreatorStatistics>)

    fun findByCreatorId(creatorId: Long): CreatorStatistics

    fun addSalesCount(creatorId: Long, count: Int)

    fun increaseSubscribeCount(creatorId: Long)

    fun decreaseSubscribeCount(creatorId: Long)

    fun deleteAllInBatch()
}

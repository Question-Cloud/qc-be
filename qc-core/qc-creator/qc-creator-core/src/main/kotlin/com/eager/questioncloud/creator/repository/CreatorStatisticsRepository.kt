package com.eager.questioncloud.creator.repository

import com.eager.questioncloud.creator.domain.CreatorStatistics

interface CreatorStatisticsRepository {
    fun save(creatorStatistics: CreatorStatistics)
    
    fun update(creatorStatistics: CreatorStatistics)
    
    fun findByCreatorId(creatorId: Long): CreatorStatistics
    
    fun getForUpdate(creatorId: Long): CreatorStatistics
    
    fun findByCreatorIdIn(creatorIds: List<Long>): Map<Long, CreatorStatistics>
    
    fun addSalesCount(creatorId: Long, count: Int)
    
    fun deleteAllInBatch()
}

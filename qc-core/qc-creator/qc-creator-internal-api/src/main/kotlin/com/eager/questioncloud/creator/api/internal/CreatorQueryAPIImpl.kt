package com.eager.questioncloud.creator.api.internal

import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import org.springframework.stereotype.Component

@Component
class CreatorQueryAPIImpl(
    private val creatorRepository: CreatorRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
) : CreatorQueryAPI {
    override fun getCreator(creatorId: Long): CreatorQueryData {
        val creator = creatorRepository.findById(creatorId)
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
        
        return CreatorQueryData(
            creator.userId,
            creator.id,
            creator.mainSubject,
            creatorStatistics.averageRateOfReview,
            creatorStatistics.salesCount,
            creatorStatistics.subscriberCount,
        )
    }
    
    override fun getCreators(creatorIds: List<Long>): List<CreatorQueryData> {
        val creators = creatorRepository.findByIdIn(creatorIds)
        val creatorStatisticsMap = creatorStatisticsRepository.findByCreatorIdIn(creatorIds)
        
        return creators.map {
            val creatorStatistics = creatorStatisticsMap.getValue(it.id)
            CreatorQueryData(
                it.userId,
                it.id,
                it.mainSubject,
                creatorStatistics.averageRateOfReview,
                creatorStatistics.salesCount,
                creatorStatistics.subscriberCount,
            )
        }
    }
    
    override fun isExistsById(creatorId: Long): Boolean {
        return creatorRepository.existsById(creatorId)
    }
}
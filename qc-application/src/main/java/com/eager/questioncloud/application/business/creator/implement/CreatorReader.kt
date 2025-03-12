package com.eager.questioncloud.application.business.creator.implement

import com.eager.questioncloud.application.business.creator.dto.CreatorInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class CreatorReader(
    private val creatorRepository: CreatorRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val subscribeRepository: SubscribeRepository,
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        val creatorProfile = creatorRepository.getCreatorProfile(creatorId)
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
        val subscriberCount = subscribeRepository.countSubscriber(creatorId)
        return CreatorInformation(
            creatorProfile,
            creatorStatistics.salesCount,
            creatorStatistics.averageRateOfReview,
            subscriberCount
        )
    }

    fun getCreatorInformation(creatorIds: List<Long>): List<CreatorInformation> {
        val creatorProfiles = creatorRepository.getCreatorProfile(creatorIds)
        val creatorStatistics = creatorStatisticsRepository.findByCreatorIdIn(creatorIds)
        val subscriberCount = subscribeRepository.countSubscriber(creatorIds)

        return creatorIds.map { creatorId ->
            val statistics = creatorStatistics[creatorId] ?: throw CoreException(Error.NOT_FOUND)

            CreatorInformation(
                creatorProfiles[creatorId] ?: throw CoreException(Error.NOT_FOUND),
                statistics.salesCount,
                statistics.averageRateOfReview,
                subscriberCount[creatorId] ?: 0
            )
        }
    }
}
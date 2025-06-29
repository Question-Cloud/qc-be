package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import org.springframework.stereotype.Component

@Component
class CreatorStatisticsInitializer(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
) {
    fun initializeCreatorStatistics(creatorId: Long) {
        creatorStatisticsRepository.save(CreatorStatistics.create(creatorId))
    }
}

package com.eager.questioncloud.application.api.creator.implement

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics.Companion.create
import org.springframework.stereotype.Component

@Component
class CreatorStatisticsInitializer(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
) {
    fun initializeCreatorStatistics(creatorId: Long) {
        creatorStatisticsRepository.save(create(creatorId))
    }
}

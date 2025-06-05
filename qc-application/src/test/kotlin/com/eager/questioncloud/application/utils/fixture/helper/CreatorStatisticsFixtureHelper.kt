package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class CreatorStatisticsFixtureHelper {
    companion object {
        fun createCreatorStatistics(
            creatorId: Long,
            creatorStatisticsRepository: CreatorStatisticsRepository
        ): CreatorStatistics {
            val creatorStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creatorId)
                .sample()
            creatorStatisticsRepository.save(creatorStatistics)
            return creatorStatistics
        }
    }
}
package com.eager.questioncloud.application.api.workspace.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CreatorStatisticsInitializerTest(
    @Autowired val creatorStatisticsInitializer: CreatorStatisticsInitializer,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터 통계를 초기화할 수 있다`() {
        // given
        val creatorId = 123L

        // when
        creatorStatisticsInitializer.initializeCreatorStatistics(creatorId)

        // then
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)

        Assertions.assertThat(creatorStatistics.creatorId).isEqualTo(creatorId)
        Assertions.assertThat(creatorStatistics.salesCount).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.reviewCount).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(0.0)
    }
}

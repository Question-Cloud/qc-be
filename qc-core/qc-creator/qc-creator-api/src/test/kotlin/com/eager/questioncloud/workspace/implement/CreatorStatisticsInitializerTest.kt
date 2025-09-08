package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.utils.DBCleaner
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
    @Autowired val dbCleaner: DBCleaner,
) {
    private val creatorId = 1L
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `크리에이터 통계를 초기화할 수 있다`() {
        //when
        creatorStatisticsInitializer.initializeCreatorStatistics(creatorId)
        
        //then
        val foundStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
        Assertions.assertThat(foundStatistics).isNotNull
        Assertions.assertThat(foundStatistics.creatorId).isEqualTo(creatorId)
        Assertions.assertThat(foundStatistics.salesCount).isEqualTo(0)
        Assertions.assertThat(foundStatistics.reviewCount).isEqualTo(0)
        Assertions.assertThat(foundStatistics.totalReviewRate).isEqualTo(0)
        Assertions.assertThat(foundStatistics.averageRateOfReview).isEqualTo(0.0)
        Assertions.assertThat(foundStatistics.subscriberCount).isEqualTo(0)
    }
}
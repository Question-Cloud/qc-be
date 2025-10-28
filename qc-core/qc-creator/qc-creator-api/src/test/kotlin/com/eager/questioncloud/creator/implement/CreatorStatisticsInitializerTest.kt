package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CreatorStatisticsInitializerTest(
    private val creatorStatisticsInitializer: CreatorStatisticsInitializer,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터 통계 초기화") {
            val creatorId = 1L
            
            When("크리에이터 통계를 초기화하면") {
                creatorStatisticsInitializer.initializeCreatorStatistics(creatorId)
                
                Then("초기값으로 통계가 생성된다") {
                    val foundStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
                    
                    foundStatistics shouldNotBe null
                    foundStatistics.creatorId shouldBe creatorId
                    foundStatistics.salesCount shouldBe 0
                    foundStatistics.reviewCount shouldBe 0
                    foundStatistics.totalReviewRate shouldBe 0
                    foundStatistics.averageRateOfReview shouldBe 0.0
                    foundStatistics.subscriberCount shouldBe 0
                }
            }
        }
    }
}
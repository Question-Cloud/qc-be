package com.eager.questioncloud.creator.handler

import com.eager.questioncloud.common.event.SubscribeEvent
import com.eager.questioncloud.common.event.SubscribeEventType
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UpdateCreatorSubscribeCountStatisticsHandlerTest(
    private val updateCreatorSubscribeCountStatisticsHandler: UpdateCreatorSubscribeCountStatisticsHandler,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("Subscribe Event") {
            val creatorId = 1L
            val originCreatorStatistics = CreatorStatistics.create(creatorId)
            creatorStatisticsRepository.save(originCreatorStatistics)
            val event = SubscribeEvent(creatorId, SubscribeEventType.SUBSCRIBE)
            
            When("구독 이벤트가 발행되어 처리되면") {
                updateCreatorSubscribeCountStatisticsHandler.handler(event)
                Then("크리에이터 통계의 구독자 수 데이터가 갱신된다.") {
                    val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
                    creatorStatistics.subscriberCount shouldBe 1
                }
            }
        }
        
        Given("UnSubscribe Event") {
            val creatorId = 1L
            
            val originSubscribeCount = 100
            val originCreatorStatistics = CreatorStatistics.create(creatorId)
            originCreatorStatistics.subscriberCount = originSubscribeCount
            creatorStatisticsRepository.save(originCreatorStatistics)
            
            val event = SubscribeEvent(creatorId, SubscribeEventType.UNSUBSCRIBE)
            
            When("구독 취소 이벤트가 발행되어 처리되면") {
                updateCreatorSubscribeCountStatisticsHandler.handler(event)
                Then("크리에이터 통계의 구독자 수 데이터가 갱신된다.") {
                    val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
                    creatorStatistics.subscriberCount shouldBe originSubscribeCount - 1
                }
            }
        }
    }
}

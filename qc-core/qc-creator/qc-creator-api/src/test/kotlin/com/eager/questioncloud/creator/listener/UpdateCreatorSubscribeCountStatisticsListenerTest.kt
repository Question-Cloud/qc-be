package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.common.event.SubscribeEvent
import com.eager.questioncloud.common.event.SubscribeEventType
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UpdateCreatorSubscribeCountStatisticsListenerTest(
    private val updateCreatorSubscribeCountStatisticsListener: UpdateCreatorSubscribeCountStatisticsListener,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("Subscribe Event") {
            val creatorScenario = CreatorScenario.create(1)
            val creator = creatorScenario.creators[0]
            creatorStatisticsRepository.save(creatorScenario.creatorStatisticses[0])
            
            val event = SubscribeEvent(creator.id, SubscribeEventType.SUBSCRIBE)
            
            When("구독 이벤트가 발행되어 처리되면") {
                updateCreatorSubscribeCountStatisticsListener.onMessage(event)
                Then("크리에이터 통계의 구독자 수 데이터가 갱신된다.") {
                    val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id)
                    creatorStatistics.subscriberCount shouldBe 1
                }
            }
        }
        
        Given("UnSubscribe Event") {
            val creatorScenario = CreatorScenario.create(1)
            val creator = creatorScenario.creators[0]
            val originSubscribeCount = 100
            creatorScenario.creatorStatisticses[0].subscriberCount = originSubscribeCount
            creatorStatisticsRepository.save(creatorScenario.creatorStatisticses[0])
            
            val event = SubscribeEvent(creator.id, SubscribeEventType.UNSUBSCRIBE)
            
            When("구독 취소 이벤트가 발행되어 처리되면") {
                updateCreatorSubscribeCountStatisticsListener.onMessage(event)
                Then("크리에이터 통계의 구독자 수 데이터가 갱신된다.") {
                    val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id)
                    creatorStatistics.subscriberCount shouldBe originSubscribeCount - 1
                }
            }
        }
    }
}

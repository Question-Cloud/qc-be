package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.test.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UpdateCreatorSalesStatisticsListenerTest(
    private val updateCreatorSalesStatisticsListener: UpdateCreatorSalesStatisticsListener,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("Update CreatorSalesStatistics By QuestionPaymentEvent") {
            val userId = 1L
            
            val creatorScenario = CreatorScenario.create(5)
            creatorScenario.creatorStatisticses.forEach { creatorStatisticsRepository.save(it) }
            
            var questionId = 1
            
            val questionQueryDatas = creatorScenario.creators.flatMap { creator ->
                val cnt = (1..5).random()
                (1..cnt).map {
                    Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                        .set(QuestionInformationQueryResult::id, questionId++)
                        .set(QuestionInformationQueryResult::creatorId, creator.id)
                        .sample()
                }
            }
            
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns questionQueryDatas
            
            val event = QuestionPaymentEvent(
                paymentId = 1L,
                orderId = UUID.randomUUID().toString(),
                buyerUserId = userId,
                questionIds = questionQueryDatas.map { it.id }.toList(),
                amount = 10000,
            )
            
            When("QuestionPaymentEvent가 발행되어 처리되면") {
                updateCreatorSalesStatisticsListener.onMessage(event)
                Then("크리에이터 통계의 판매 관련 데이터가 갱신된다.") {
                    creatorScenario.creators.forEach { creator ->
                        val statistics = creatorStatisticsRepository.findByCreatorId(creator.id)
                        statistics.salesCount shouldBeEqual questionQueryDatas.stream().filter { it.creatorId == creator.id }.count()
                            .toInt()
                    }
                }
            }
        }
    }
}

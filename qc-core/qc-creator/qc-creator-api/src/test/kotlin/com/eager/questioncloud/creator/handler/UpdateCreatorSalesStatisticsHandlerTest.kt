package com.eager.questioncloud.creator.handler

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.utils.Fixture
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
class UpdateCreatorSalesStatisticsHandlerTest(
    private val updateCreatorSalesStatisticsHandler: UpdateCreatorSalesStatisticsHandler,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("Question Review Event") {
            val userId = 1L
            
            val creator1Id = 1L
            val creator2Id = 2L
            
            val originCreator1Statistics = CreatorStatistics.create(creator1Id)
            val originCreator2Statistics = CreatorStatistics.create(creator2Id)
            
            val questionIds = listOf(1L, 2L, 3L)
            
            creatorStatisticsRepository.save(originCreator1Statistics)
            creatorStatisticsRepository.save(originCreator2Statistics)
            
            val question1QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionIds[0])
                .set(QuestionInformationQueryResult::creatorId, creator1Id)
                .sample()
            
            val question2QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionIds[1])
                .set(QuestionInformationQueryResult::creatorId, creator1Id)
                .sample()
            
            val question3QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionIds[2])
                .set(QuestionInformationQueryResult::creatorId, creator2Id)
                .sample()
            
            val questionQueryDatas = mutableListOf<QuestionInformationQueryResult>()
            questionQueryDatas.addAll(listOf(question1QueryData, question2QueryData, question3QueryData))
            
            val event = QuestionPaymentEvent(
                orderId = UUID.randomUUID().toString(),
                buyerUserId = userId,
                questionIds = questionIds,
                amount = 10000,
                questionPaymentCoupon = null
            )
            
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns questionQueryDatas
            
            When("QuestionPaymentEvent가 발행되어 처리되면") {
                updateCreatorSalesStatisticsHandler.updateCreatorStatistics(event)
                Then("크리에이터 통계의 판매 관련 데이터가 갱신된다.") {
                    val creator1Statistics = creatorStatisticsRepository.findByCreatorId(creator1Id)
                    val creator2Statistics = creatorStatisticsRepository.findByCreatorId(creator2Id)
                    
                    creator1Statistics.salesCount shouldBeEqual questionQueryDatas.stream().filter { it.creatorId == creator1Id }.count()
                        .toInt()
                    
                    creator2Statistics.salesCount shouldBeEqual questionQueryDatas.stream().filter { it.creatorId == creator2Id }.count()
                        .toInt()
                }
            }
        }
    }
}

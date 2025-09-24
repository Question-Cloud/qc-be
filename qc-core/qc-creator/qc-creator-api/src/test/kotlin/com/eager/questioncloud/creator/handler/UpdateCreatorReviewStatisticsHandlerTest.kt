package com.eager.questioncloud.creator.handler

import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.common.event.ReviewEventType
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
import kotlin.math.abs

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UpdateCreatorReviewStatisticsHandlerTest(
    private val updateCreatorReviewStatisticsHandler: UpdateCreatorReviewStatisticsHandler,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("Register Review Event") {
            val creatorId = 1L
            val questionId = 1L
            val originStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creatorId)
                .sample()
            creatorStatisticsRepository.save(originStatistics)
            val reviewEvent = ReviewEvent.create(questionId, 3, ReviewEventType.REGISTER)
            
            every {
                questionQueryAPI.getQuestionInformation(any<Long>())
            } returns Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionId)
                .set(QuestionInformationQueryResult::creatorId, creatorId)
                .sample()
            
            When("Register Review Event가 발행되어 처리되면") {
                updateCreatorReviewStatisticsHandler.updateCreatorReviewStatistics(reviewEvent)
                Then("크리에이터 통계의 리뷰 관련 갱신된다.") {
                    val statistics = creatorStatisticsRepository.findByCreatorId(creatorId)
                    abs(originStatistics.totalReviewRate - statistics.totalReviewRate) shouldBeEqual reviewEvent.varianceRate
                }
            }
        }
        
        Given("Modify Review Event") {
            val creatorId = 1L
            val questionId = 1L
            val originStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creatorId)
                .sample()
            creatorStatisticsRepository.save(originStatistics)
            val reviewEvent = ReviewEvent.create(questionId, 3, ReviewEventType.MODIFY)
            
            every {
                questionQueryAPI.getQuestionInformation(any<Long>())
            } returns Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionId)
                .set(QuestionInformationQueryResult::creatorId, creatorId)
                .sample()
            
            When("Modify Review Event가 발행되어 처리되면") {
                updateCreatorReviewStatisticsHandler.updateCreatorReviewStatistics(reviewEvent)
                Then("크리에이터 통계의 리뷰 관련 갱신된다.") {
                    val statistics = creatorStatisticsRepository.findByCreatorId(creatorId)
                    abs(originStatistics.totalReviewRate - statistics.totalReviewRate) shouldBeEqual reviewEvent.varianceRate
                }
            }
        }
        
        Given("Delete Review Event") {
            val creatorId = 1L
            val questionId = 1L
            val originStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creatorId)
                .sample()
            creatorStatisticsRepository.save(originStatistics)
            val reviewEvent = ReviewEvent.create(questionId, 3, ReviewEventType.DELETE)
            
            every {
                questionQueryAPI.getQuestionInformation(any<Long>())
            } returns Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionId)
                .set(QuestionInformationQueryResult::creatorId, creatorId)
                .sample()
            
            When("Delete Review Event가 발행되어 처리되면") {
                updateCreatorReviewStatisticsHandler.updateCreatorReviewStatistics(reviewEvent)
                Then("크리에이터 통계의 리뷰 관련 데이터가 갱신된다.") {
                    val statistics = creatorStatisticsRepository.findByCreatorId(creatorId)
                    abs(originStatistics.totalReviewRate - statistics.totalReviewRate) shouldBeEqual reviewEvent.varianceRate
                }
            }
        }
    }
}

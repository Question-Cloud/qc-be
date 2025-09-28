package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.Promotion
import com.eager.questioncloud.payment.enums.PromotionType
import com.eager.questioncloud.payment.repository.PromotionRepository
import com.eager.questioncloud.payment.scenario.QuestionPaymentScenario
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionOrderGeneratorTest(
    private val questionOrderGenerator: QuestionOrderGenerator,
    private val promotionRepository: PromotionRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 구매 가능한 문제들로 주문을 생성할 때") {
            val userId = 1L
            val questions = (1..10).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .sample()
            }
            
            every { questionQueryAPI.isOwned(any(), any<List<Long>>()) } returns false
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns questions
            
            When("주문 생성을 요청하면") {
                val questionOrder = questionOrderGenerator.generateQuestionOrder(userId, questions.map { it.id })
                
                Then("요청한 문제들로 주문이 생성된다") {
                    questionOrder.questionIds shouldContainExactlyInAnyOrder questions.map { it.id }
                }
            }
        }
        
        Given("사용자가 프로모션 중인 문제로 주문을 생성할 때") {
            val userId = 1L
            val promotionQuestions = QuestionPaymentScenario.create(5).questionInformationQueryResult
            val promotionMap = promotionQuestions.map {
                promotionRepository.save(
                    Promotion(
                        questionId = it.id,
                        promotionType = PromotionType.FIXED,
                        title = "promtion ${it.id}",
                        value = (100..it.price).random(),
                        isActive = true
                    )
                )
            }.associateBy { it.questionId }
            
            every { questionQueryAPI.isOwned(any(), any<List<Long>>()) } returns false
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns promotionQuestions
            
            When("주문 생성을 요청하면") {
                val questionOrder = questionOrderGenerator.generateQuestionOrder(userId, promotionQuestions.map { it.id })
                Then("프로모션이 적용되어 주문이 생성된다") {
                    questionOrder.questionIds shouldContainExactlyInAnyOrder promotionQuestions.map { it.id }
                    
                    questionOrder.items.forEach {
                        val promotion = promotionMap.getValue(it.questionId)
                        it.promotionDiscountAmount shouldBe promotion.toDiscountable().getDiscountAmount(it.originalPrice)
                        it.realPrice shouldBe it.originalPrice - it.promotionDiscountAmount
                    }
                }
            }
        }
        
        Given("비활성화된 문제가 포함된 주문을 생성할 때") {
            val userId = 1L
            
            val questions = (1..10).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .sample()
            }
            
            val availableQuestions = questions.subList(0, questions.size / 2)
            
            every { questionQueryAPI.isOwned(any(), any<List<Long>>()) } returns false
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns availableQuestions
            
            When("주문 생성을 요청하면") {
                Then("UNAVAILABLE_QUESTION 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionOrderGenerator.generateQuestionOrder(userId, questions.map { it.id })
                    }
                    exception.error shouldBe Error.UNAVAILABLE_QUESTION
                }
            }
        }
        
        Given("사용자가 이미 구매한 문제로 주문을 생성할 때") {
            val userId = 1L
            val alreadyOwnedQuestionId = 1L
            val normalQuestionId = 2L
            val questionIds = listOf(alreadyOwnedQuestionId, normalQuestionId)
            
            every { questionQueryAPI.isOwned(any(), any<List<Long>>()) } returns true
            
            When("주문 생성을 요청하면") {
                Then("ALREADY_OWN_QUESTION 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionOrderGenerator.generateQuestionOrder(userId, questionIds)
                    }
                    exception.error shouldBe Error.ALREADY_OWN_QUESTION
                }
            }
        }
    }
}
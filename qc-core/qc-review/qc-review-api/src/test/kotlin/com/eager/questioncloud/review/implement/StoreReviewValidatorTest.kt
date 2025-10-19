package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.review.ReviewScenario
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class StoreReviewValidatorTest(
    private val storeReviewValidator: StoreReviewValidator,
    private val questionReviewRepository: QuestionReviewRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("리뷰를 등록할 수 있는 조건일 때") {
            val userId = 100L
            val questionId = 200L
            val command = RegisterReviewCommand(
                reviewerId = userId,
                questionId = questionId,
                comment = "좋은 문제입니다",
                rate = 5
            )
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns true
            
            When("리뷰 등록을 검증하면") {
                Then("예외가 발생하지 않는다") {
                    shouldNotThrowAny {
                        storeReviewValidator.validate(command)
                    }
                }
            }
        }
        
        Given("이용할 수 없는 문제가 있을 때") {
            val userId = 101L
            val questionId = 201L
            val command = RegisterReviewCommand(
                reviewerId = userId,
                questionId = questionId,
                comment = "리뷰입니다",
                rate = 4
            )
            
            every { questionQueryAPI.isAvailable(questionId) } returns false
            
            When("리뷰 등록을 검증하면") {
                Then("CoreException(Error.UNAVAILABLE_QUESTION)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        storeReviewValidator.validate(command)
                    }
                    exception.error shouldBe Error.UNAVAILABLE_QUESTION
                }
            }
        }
        
        Given("소유하지 않은 문제가 있을 때") {
            val userId = 102L
            val questionId = 202L
            val command = RegisterReviewCommand(
                reviewerId = userId,
                questionId = questionId,
                comment = "리뷰입니다",
                rate = 3
            )
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns false
            
            When("리뷰 등록을 검증하면") {
                Then("CoreException(Error.NOT_OWNED_QUESTION)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        storeReviewValidator.validate(command)
                    }
                    exception.error shouldBe Error.NOT_OWNED_QUESTION
                }
            }
        }
        
        Given("이미 리뷰를 작성한 문제가 있을 때") {
            val userId = 103L
            val questionId = 203L
            val command = RegisterReviewCommand(
                reviewerId = userId,
                questionId = questionId,
                comment = "또 다른 리뷰",
                rate = 5
            )
            
            val scenario = ReviewScenario.createMyReveiw(questionId = questionId, userId = userId)
            questionReviewRepository.save(scenario.reviews[0])
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns true
            
            When("리뷰 등록을 검증하면") {
                Then("CoreException(Error.ALREADY_REGISTER_REVIEW)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        storeReviewValidator.validate(command)
                    }
                    exception.error shouldBe Error.ALREADY_REGISTER_REVIEW
                }
            }
        }
    }
}
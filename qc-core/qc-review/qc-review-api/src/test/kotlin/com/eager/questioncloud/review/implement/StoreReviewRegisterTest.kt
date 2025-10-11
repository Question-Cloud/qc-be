package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class StoreReviewRegisterTest(
    private val storeReviewRegister: StoreReviewRegister,
    private val questionReviewRepository: QuestionReviewRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("리뷰 등록") {
            val questionId = 1L
            val userId = 1L
            val registerReviewCommand = RegisterReviewCommand(questionId, userId, "좋은 문제입니다", 5)
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns true
            
            When("리뷰를 등록하면") {
                storeReviewRegister.register(registerReviewCommand)
                Then("리뷰가 등록된다") {
                    val isWritten = questionReviewRepository.isWritten(userId, questionId)
                    isWritten shouldBe true
                    
                    val savedReview = questionReviewRepository.findByQuestionIdAndUserId(questionId, userId)
                    savedReview.comment shouldBe "좋은 문제입니다"
                    savedReview.rate shouldBe 5
                }
            }
        }
        
        Given("사용할 수 없는 문제") {
            val questionId = 1L
            val userId = 1L
            
            val registerReviewCommand = RegisterReviewCommand(questionId, userId, "좋은 문제입니다", 5)
            
            every { questionQueryAPI.isAvailable(questionId) } returns false
            
            When("리뷰를 등록하면") {
                val exception = shouldThrow<CoreException> {
                    storeReviewRegister.register(registerReviewCommand)
                }
                Then("UNAVAILABLE_QUESTION 에러가 발생한다") {
                    exception.error shouldBe Error.UNAVAILABLE_QUESTION
                }
            }
        }
        
        Given("소유하지 않은 문제") {
            val questionId = 1L
            val userId = 1L
            
            val registerReviewCommand = RegisterReviewCommand(questionId, userId, "좋은 문제입니다", 5)
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns false
            
            When("리뷰를 등록하면") {
                val exception = shouldThrow<CoreException> {
                    storeReviewRegister.register(registerReviewCommand)
                }
                Then("NOT_OWNED_QUESTION 에러가 발생한다") {
                    exception.error shouldBe Error.NOT_OWNED_QUESTION
                }
            }
        }
        
        Given("이미 리뷰를 작성한 문제") {
            val questionId = 1L
            val userId = 1L
            
            val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)
            questionReviewRepository.save(questionReview)
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns true
            
            val registerReviewCommand = RegisterReviewCommand(questionId, userId, "좋은 문제입니다", 5)
            
            When("중복 리뷰를 등록하면") {
                val exception = shouldThrow<CoreException> {
                    storeReviewRegister.register(registerReviewCommand)
                }
                Then("ALREADY_REGISTER_REVIEW 에러가 발생한다") {
                    exception.error shouldBe Error.ALREADY_REGISTER_REVIEW
                }
            }
        }
        
        Given("리뷰 중복 작성 동시성 테스트") {
            val questionId = 1L
            val userId = 1L
            val registerReviewCommand = RegisterReviewCommand(questionId, userId, "좋은 문제입니다", 5)
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns true
            
            When("100개의 스레드에서 동시에 리뷰를 등록하면") {
                val threadCount = 100
                val executorService = Executors.newFixedThreadPool(threadCount)
                val latch = CountDownLatch(threadCount)
                
                for (i in 0..<threadCount) {
                    executorService.execute {
                        try {
                            storeReviewRegister.register(registerReviewCommand)
                        } catch (ignored: Exception) {
                        } finally {
                            latch.countDown()
                        }
                    }
                }
                
                latch.await()
                
                Then("하나의 리뷰만 등록된다") {
                    val reviewCount = questionReviewRepository.countByQuestionId(questionId)
                    reviewCount shouldBe 1
                }
            }
        }
    }
}
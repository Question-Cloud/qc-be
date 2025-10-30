package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.ReviewScenario
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
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
class StoreReviewReaderTest(
    private val storeReviewReader: StoreReviewReader,
    private val questionReviewRepository: QuestionReviewRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("문제 리뷰 개수 조회") {
            val questionId = 1L
            val reviewScenario = ReviewScenario.create(questionId)
            reviewScenario.reviews.forEach { questionReviewRepository.save(it) }
            When("문제 리뷰 개수를 조회하면") {
                val count = storeReviewReader.count(questionId)
                Then("문제의 리뷰 개수가 조회된다.") {
                    count shouldBe reviewScenario.reviews.size.toLong()
                }
            }
        }
        
        Given("문제 리뷰 조회") {
            val questionId = 1L
            val userId = 1L
            val reviewScenario = ReviewScenario.create(questionId)
            val savedReview = reviewScenario.reviews.map { questionReviewRepository.save(it) }
            
            every { userQueryAPI.getUsers(any()) } returns reviewScenario.userQueryDatas
            
            When("문제 리뷰를 조회하면") {
                val reviews = storeReviewReader.getQuestionReviewDetails(questionId, userId, PagingInformation.max)
                Then("문제 리뷰 데이터가 조회된다.") {
                    reviews.size shouldBe reviewScenario.reviews.size
                    reviews.forEach { review ->
                        val expectedReview = savedReview.find { it.id == review.id }!!
                        val writer = reviewScenario.userQueryDatas.find { it.userId == expectedReview.reviewerId }!!
                        
                        review.comment shouldBe expectedReview.comment
                        review.rate shouldBe expectedReview.rate
                        review.reviewerName shouldBe writer.name
                    }
                }
            }
        }
        
        Given("내가 작성한 리뷰가 있을 때") {
            val questionId = 1L
            val me = 1L
            val myReviewScenario = ReviewScenario.createMyReveiw(questionId, me)
            val savedReview = questionReviewRepository.save(myReviewScenario.reviews[0])
            When("내가 작성한 리뷰를 조회하면") {
                val myReview = storeReviewReader.getMyQuestionReview(questionId, me)
                Then("리뷰 데이터가 조회된다.") {
                    myReview.comment shouldBe savedReview.comment
                    myReview.rate shouldBe savedReview.rate
                }
            }
        }
    }
}

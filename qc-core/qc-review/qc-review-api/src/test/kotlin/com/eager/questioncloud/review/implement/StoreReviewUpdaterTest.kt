package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.ReviewScenario
import com.eager.questioncloud.review.command.ModifyReviewCommand
import com.eager.questioncloud.review.repository.QuestionReviewRepository
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
class StoreReviewUpdaterTest(
    private val storeReviewUpdater: StoreReviewUpdater,
    private val questionReviewRepository: QuestionReviewRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("작성한 리뷰가 있을 때") {
            val questionId = 1L
            val reviewScenario = ReviewScenario.create(questionId)
            val savedReviews = reviewScenario.reviews.map { questionReviewRepository.save(it) }
            val savedMyReview = savedReviews[0]
            
            val newComment = "수정 된 코멘트"
            val newRate = 4
            
            val modifyReviewCommand = ModifyReviewCommand(savedMyReview.id, savedMyReview.reviewerId, newComment, newRate)
            When("리뷰를 수정하면") {
                val result = storeReviewUpdater.modify(modifyReviewCommand)
                Then("리뷰가 수정되고 수정된 리뷰 정보가 반환된다") {
                    result.first shouldBe questionId
                    result.second shouldBe newRate - savedMyReview.rate
                    
                    val updatedReview = questionReviewRepository.findByIdAndUserId(savedMyReview.id, savedMyReview.reviewerId)
                    updatedReview.comment shouldBe newComment
                    updatedReview.rate shouldBe newRate
                }
            }
        }
    }
}
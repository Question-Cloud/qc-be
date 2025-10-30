package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.ReviewScenario
import com.eager.questioncloud.review.command.DeleteReviewCommand
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
class StoreReviewRemoverTest(
    private val storeReviewRemover: StoreReviewRemover,
    private val questionReviewRepository: QuestionReviewRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("작성한 리뷰가 있을 때") {
            val questionId = 1L
            val reviewScenario = ReviewScenario.create(questionId, 5);
            val reviews = reviewScenario.reviews.map { questionReviewRepository.save(it) }
            
            val myReview = reviews[0]
            val myUserId = reviews[0].reviewerId
            
            val deleteReviewCommand = DeleteReviewCommand(myReview.id, myUserId)
            When("리뷰를 삭제하면") {
                val result = storeReviewRemover.delete(deleteReviewCommand)
                Then("리뷰가 삭제되고 삭제된 리뷰 정보가 반환된다") {
                    result.first shouldBe questionId
                    result.second shouldBe myReview.rate
                    
                    questionReviewRepository.isWritten(myUserId, questionId) shouldBe false
                }
            }
        }
    }
}
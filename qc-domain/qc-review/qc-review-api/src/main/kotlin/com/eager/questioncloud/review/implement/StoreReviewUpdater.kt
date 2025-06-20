package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewUpdater(
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun modify(reviewId: Long, userId: Long, comment: String, rate: Int): Pair<Long, Int> {
        val questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId)
        val varianceRate = questionReview.modify(comment, rate)
        questionReviewRepository.save(questionReview)
        return Pair(questionReview.questionId, varianceRate)
    }
}
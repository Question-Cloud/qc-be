package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewRemover(
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun delete(reviewId: Long, userId: Long): Pair<Long, Int> {
        val questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId)
        questionReview.delete()
        questionReviewRepository.save(questionReview)
        return Pair(questionReview.questionId, questionReview.rate)
    }
}
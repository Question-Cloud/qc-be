package com.eager.questioncloud.application.business.review.implement

import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class HubReviewRemover(
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun delete(reviewId: Long, userId: Long): Pair<Long, Int> {
        val questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId)
        questionReview.delete()
        questionReviewRepository.save(questionReview)
        return Pair(questionReview.questionId, questionReview.rate)
    }
}
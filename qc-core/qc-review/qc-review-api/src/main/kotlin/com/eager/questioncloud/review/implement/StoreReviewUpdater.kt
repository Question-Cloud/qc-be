package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.command.ModifyReviewCommand
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewUpdater(
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun modify(command: ModifyReviewCommand): Pair<Long, Int> {
        val questionReview = questionReviewRepository.findByIdAndUserId(command.reviewId, command.reviewerId)
        val varianceRate = questionReview.modify(command.comment, command.rate)
        questionReviewRepository.save(questionReview)
        return Pair(questionReview.questionId, varianceRate)
    }
}
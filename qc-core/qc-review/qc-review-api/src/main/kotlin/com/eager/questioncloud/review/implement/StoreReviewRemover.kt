package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.command.DeleteReviewCommand
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewRemover(
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun delete(command: DeleteReviewCommand): Pair<Long, Int> {
        val questionReview = questionReviewRepository.findByIdAndUserId(command.reviewId, command.reviewerId)
        questionReview.delete()
        questionReviewRepository.save(questionReview)
        return Pair(questionReview.questionId, questionReview.rate)
    }
}
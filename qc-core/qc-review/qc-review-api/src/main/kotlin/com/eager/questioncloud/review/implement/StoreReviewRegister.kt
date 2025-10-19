package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewRegister(
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun register(command: RegisterReviewCommand): QuestionReview {
        val questionReview = QuestionReview.create(command.questionId, command.reviewerId, command.comment, command.rate)
        return questionReviewRepository.save(questionReview)
    }
}

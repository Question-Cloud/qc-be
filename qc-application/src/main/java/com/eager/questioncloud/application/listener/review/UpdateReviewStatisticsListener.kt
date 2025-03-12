package com.eager.questioncloud.application.listener.review

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import com.eager.questioncloud.lock.LockKeyGenerator
import com.eager.questioncloud.lock.LockManager
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class UpdateReviewStatisticsListener(
    private val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository,
    private val lockManager: LockManager,
) {
    @SqsListener("update-question-review-statistics.fifo")
    fun updateByRegisteredReview(event: ReviewEvent) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.questionId)
        ) {
            val reviewStatistics = questionReviewStatisticsRepository.get(event.questionId)
            when (event.reviewEventType) {
                ReviewEventType.REGISTER -> reviewStatistics.updateByNewReview(event.varianceRate)
                ReviewEventType.MODIFY -> reviewStatistics.updateByModifyReview(event.varianceRate)
                ReviewEventType.DELETE -> reviewStatistics.updateByDeleteReview(event.varianceRate)
            }
            questionReviewStatisticsRepository.save(reviewStatistics)
        }
    }
}

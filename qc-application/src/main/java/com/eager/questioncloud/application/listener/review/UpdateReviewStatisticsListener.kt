package com.eager.questioncloud.application.listener.review

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class UpdateReviewStatisticsListener(
    private val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository
) {
    @SqsListener("update-question-review-statistics.fifo")
    fun updateByRegisteredReview(event: ReviewEvent) {
        val reviewStatistics = questionReviewStatisticsRepository.getForUpdate(event.questionId)
        when (event.reviewEventType) {
            ReviewEventType.REGISTER -> reviewStatistics.updateByNewReview(event.varianceRate)
            ReviewEventType.MODIFY -> reviewStatistics.updateByModifyReview(event.varianceRate)
            ReviewEventType.DELETE -> reviewStatistics.updateByDeleteReview(event.varianceRate)
        }
        questionReviewStatisticsRepository.save(reviewStatistics)
    }
}

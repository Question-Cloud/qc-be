package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.event.model.ReviewEvent
import com.eager.questioncloud.event.model.ReviewEventType
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateCreatorReviewStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionQueryAPI: QuestionQueryAPI
) {
    @SqsListener("update-creator-review-statistics.fifo")
    @IdempotentEvent
    fun updateCreatorReviewStatistics(@Payload event: ReviewEvent) {
        val question = questionQueryAPI.getQuestionInformation(event.questionId)
        val creatorStatistics = creatorStatisticsRepository.getForUpdate(question.creatorId)
        when (event.reviewEventType) {
            ReviewEventType.REGISTER -> creatorStatistics.updateReviewStatisticsByRegisteredReview(event.varianceRate)
            ReviewEventType.MODIFY -> creatorStatistics.updateReviewStatisticsByModifiedReview(event.varianceRate)
            ReviewEventType.DELETE -> creatorStatistics.updateReviewStatisticsByDeletedReview(event.varianceRate)
        }
        creatorStatisticsRepository.update(creatorStatistics)
    }
}
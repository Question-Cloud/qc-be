package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.common.event.*
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
@QueueListener(queueName = "update-creator-review-statistics", type = ReviewEvent::class)
class UpdateCreatorReviewStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionQueryAPI: QuestionQueryAPI
) : MessageListener<ReviewEvent> {
    
    @IdempotentEvent
    override fun onMessage(event: ReviewEvent) {
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
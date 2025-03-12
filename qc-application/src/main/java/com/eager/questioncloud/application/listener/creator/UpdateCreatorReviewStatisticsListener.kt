package com.eager.questioncloud.application.listener.creator

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.lock.LockKeyGenerator
import com.eager.questioncloud.lock.LockManager
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateCreatorReviewStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionRepository: QuestionRepository,
    private val lockManager: LockManager,
) {
    @SqsListener("update-creator-review-statistics.fifo")
    fun updateCreatorReviewStatistics(@Payload event: ReviewEvent) {
        val question = questionRepository.get(event.questionId)
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.creatorId)
        ) {
            val creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.creatorId)
            when (event.reviewEventType) {
                ReviewEventType.REGISTER -> creatorStatistics.updateReviewStatisticsByRegisteredReview(event.varianceRate)
                ReviewEventType.MODIFY -> creatorStatistics.updateReviewStatisticsByModifiedReview(event.varianceRate)
                ReviewEventType.DELETE -> creatorStatistics.updateReviewStatisticsByDeletedReview(event.varianceRate)
            }
            creatorStatisticsRepository.save(creatorStatistics)
        }
    }
}
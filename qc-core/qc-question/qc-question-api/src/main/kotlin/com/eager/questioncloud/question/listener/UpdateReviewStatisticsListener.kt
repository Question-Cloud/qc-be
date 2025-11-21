package com.eager.questioncloud.question.listener

import com.eager.questioncloud.common.event.*
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
@QueueListener(queueName = "update-question-review-statistics", type = ReviewEvent::class)
class UpdateReviewStatisticsListener(
    private val questionMetadataRepository: QuestionMetadataRepository,
) : MessageListener<ReviewEvent> {
    
    @IdempotentEvent
    override fun onMessage(event: ReviewEvent) {
        val questionMetadata = questionMetadataRepository.getForUpdate(event.questionId)
        when (event.reviewEventType) {
            ReviewEventType.REGISTER -> questionMetadata.updateByNewReview(event.varianceRate)
            ReviewEventType.MODIFY -> questionMetadata.updateByModifyReview(event.varianceRate)
            ReviewEventType.DELETE -> questionMetadata.updateByDeleteReview(event.varianceRate)
        }
        questionMetadataRepository.update(questionMetadata)
    }
}
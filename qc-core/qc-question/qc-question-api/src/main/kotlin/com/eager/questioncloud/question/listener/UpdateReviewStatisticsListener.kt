package com.eager.questioncloud.question.listener

import com.eager.questioncloud.common.event.ReviewEventPayload
import com.eager.questioncloud.common.event.ReviewEventType
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateReviewStatisticsListener(
    private val questionMetadataRepository: QuestionMetadataRepository,
) {
    @SqsListener("update-question-review-statistics.fifo")
    @IdempotentEvent
    @Transactional
    fun updateByRegisteredReview(event: ReviewEventPayload) {
        val questionMetadata = questionMetadataRepository.getForUpdate(event.questionId)
        when (event.reviewEventType) {
            ReviewEventType.REGISTER -> questionMetadata.updateByNewReview(event.varianceRate)
            ReviewEventType.MODIFY -> questionMetadata.updateByModifyReview(event.varianceRate)
            ReviewEventType.DELETE -> questionMetadata.updateByDeleteReview(event.varianceRate)
        }
        questionMetadataRepository.update(questionMetadata)
    }
}

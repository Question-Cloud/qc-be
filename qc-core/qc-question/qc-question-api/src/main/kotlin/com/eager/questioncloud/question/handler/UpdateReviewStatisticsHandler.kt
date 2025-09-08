package com.eager.questioncloud.question.handler

import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.common.event.ReviewEventType
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateReviewStatisticsHandler(
    private val questionMetadataRepository: QuestionMetadataRepository,
) {
    @Transactional
    fun updateByRegisteredReview(event: ReviewEvent) {
        val questionMetadata = questionMetadataRepository.getForUpdate(event.questionId)
        when (event.reviewEventType) {
            ReviewEventType.REGISTER -> questionMetadata.updateByNewReview(event.varianceRate)
            ReviewEventType.MODIFY -> questionMetadata.updateByModifyReview(event.varianceRate)
            ReviewEventType.DELETE -> questionMetadata.updateByDeleteReview(event.varianceRate)
        }
        questionMetadataRepository.update(questionMetadata)
    }
}

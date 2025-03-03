package com.eager.questioncloud.core.domain.review.implement

import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import com.eager.questioncloud.lock.LockKeyGenerator
import com.eager.questioncloud.lock.LockManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class QuestionReviewStatisticsUpdater(
    private val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository,
    private val lockManager: LockManager,
) {
    @EventListener
    fun updateByRegisteredReview(event: RegisteredReviewEvent) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.questionId)
        ) {
            val reviewStatistics = questionReviewStatisticsRepository.get(event.questionId)
            reviewStatistics.updateByNewReview(event.rate)
            questionReviewStatisticsRepository.save(reviewStatistics)
        }
    }

    @EventListener
    fun updateByModifiedReview(event: ModifiedReviewEvent) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.questionId)
        ) {
            val reviewStatistics = questionReviewStatisticsRepository.get(event.questionId)
            reviewStatistics.updateByModifyReview(event.varianceRate)
            questionReviewStatisticsRepository.save(reviewStatistics)
        }
    }

    @EventListener
    fun updateByDeletedReview(event: DeletedReviewEvent) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.questionId)
        ) {
            val reviewStatistics = questionReviewStatisticsRepository.get(event.questionId)
            reviewStatistics.updateByDeleteReview(event.rate)
            questionReviewStatisticsRepository.save(reviewStatistics)
        }
    }
}

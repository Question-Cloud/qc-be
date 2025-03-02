package com.eager.questioncloud.core.domain.creator.implement

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent
import com.eager.questioncloud.core.domain.subscribe.event.SubscribedEvent
import com.eager.questioncloud.core.domain.subscribe.event.UnsubscribedEvent
import com.eager.questioncloud.lock.LockKeyGenerator
import com.eager.questioncloud.lock.LockManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CreatorStatisticsProcessor(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionRepository: QuestionRepository,
    private val lockManager: LockManager,
) {
    fun initializeCreatorStatistics(creatorId: Long) {
        creatorStatisticsRepository.save(create(creatorId))
    }

    @EventListener
    fun updateCreatorReviewStatistics(event: RegisteredReviewEvent) {
        val question = questionRepository.get(event.questionId)
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.creatorId)
        ) {
            val creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.creatorId)
            creatorStatistics.updateReviewStatisticsByRegisteredReview(event.rate)
            creatorStatisticsRepository.save(creatorStatistics)
        }
    }

    @EventListener
    fun updateCreatorReviewStatistics(event: ModifiedReviewEvent) {
        val question = questionRepository.get(event.questionId)
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.creatorId)
        ) {
            val creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.creatorId)
            creatorStatistics.updateReviewStatisticsByModifiedReview(event.varianceRate)
            creatorStatisticsRepository.save(creatorStatistics)
        }
    }

    @EventListener
    fun updateCreatorReviewStatistics(event: DeletedReviewEvent) {
        val question = questionRepository.get(event.questionId)
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.creatorId)
        ) {
            val creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.creatorId)
            creatorStatistics.updateReviewStatisticsByDeletedReview(event.rate)
            creatorStatisticsRepository.save(creatorStatistics)
        }
    }

    @EventListener
    fun increaseSubscribeCount(event: SubscribedEvent) {
        creatorStatisticsRepository.increaseSubscribeCount(event.creatorId)
    }

    @EventListener
    fun decreaseSubscribeCount(event: UnsubscribedEvent) {
        creatorStatisticsRepository.decreaseSubscribeCount(event.creatorId)
    }
}

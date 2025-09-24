package com.eager.questioncloud.creator.handler

import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.common.event.ReviewEventType
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateCreatorReviewStatisticsHandler(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionQueryAPI: QuestionQueryAPI
) {
    @Transactional
    fun updateCreatorReviewStatistics(event: ReviewEvent) {
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
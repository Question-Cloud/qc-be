package com.eager.questioncloud.application.business.creator.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.business.review.event.ReviewEvent
import com.eager.questioncloud.application.business.review.event.ReviewEventType
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.lock.LockKeyGenerator
import com.eager.questioncloud.lock.LockManager
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class CreatorStatisticsProcessor(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionRepository: QuestionRepository,
    private val lockManager: LockManager,
) {
    fun initializeCreatorStatistics(creatorId: Long) {
        creatorStatisticsRepository.save(create(creatorId))
    }

    @SqsListener("update-creator-sales-statistics.fifo")
    fun updateCreatorStatistics(@Payload event: QuestionPaymentEvent) {
        val questions = questionRepository.getQuestionsByQuestionIds(event.questionPayment.order.questionIds)
        val countQuestionByCreator = questions
            .stream()
            .collect(Collectors.groupingBy(Question::creatorId, Collectors.counting()))

        countQuestionByCreator
            .forEach { (creatorId: Long, count: Long) ->
                creatorStatisticsRepository.addSalesCount(
                    creatorId,
                    count.toInt()
                )
            }
    }

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

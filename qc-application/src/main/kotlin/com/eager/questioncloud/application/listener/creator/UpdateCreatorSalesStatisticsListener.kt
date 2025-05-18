package com.eager.questioncloud.application.listener.creator

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.event.IdempotentEvent
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class UpdateCreatorSalesStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionRepository: QuestionRepository,
) {
    @SqsListener("update-creator-sales-statistics.fifo")
    @IdempotentEvent
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
}
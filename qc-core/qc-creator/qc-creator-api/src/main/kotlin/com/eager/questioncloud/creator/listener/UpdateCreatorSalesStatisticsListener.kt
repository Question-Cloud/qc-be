package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class UpdateCreatorSalesStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    @SqsListener("update-creator-sales-statistics.fifo")
    @IdempotentEvent
    fun updateCreatorStatistics(@Payload event: QuestionPaymentEvent) {
        val questions = questionQueryAPI.getQuestionInformation(event.data.questionIds)
        val countQuestionByCreator = questions
            .stream()
            .collect(Collectors.groupingBy(QuestionInformationQueryResult::creatorId, Collectors.counting()))

        countQuestionByCreator
            .forEach { (creatorId: Long, count: Long) ->
                creatorStatisticsRepository.addSalesCount(
                    creatorId,
                    count.toInt()
                )
            }
    }
}
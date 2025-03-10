package com.eager.questioncloud.application.business.creator.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class CreatorStatisticsUpdater(
    private val questionRepository: QuestionRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
) {
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
}

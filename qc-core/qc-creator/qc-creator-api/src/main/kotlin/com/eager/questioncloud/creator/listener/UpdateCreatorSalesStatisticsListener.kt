package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.common.event.IdempotentEvent
import com.eager.questioncloud.common.event.MessageListener
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.common.event.QueueListener
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Profile("prod", "local")
@Component
@QueueListener(queueName = "update-creator-sales-statistics", type = QuestionPaymentEvent::class)
class UpdateCreatorSalesStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) : MessageListener<QuestionPaymentEvent> {
    
    @IdempotentEvent
    override fun onMessage(event: QuestionPaymentEvent) {
        val questions = questionQueryAPI.getQuestionInformation(event.questionIds)
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
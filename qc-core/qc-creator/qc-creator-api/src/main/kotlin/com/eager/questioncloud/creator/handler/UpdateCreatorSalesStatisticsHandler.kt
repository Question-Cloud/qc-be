package com.eager.questioncloud.creator.handler

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class UpdateCreatorSalesStatisticsHandler(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun updateCreatorStatistics(event: QuestionPaymentEvent) {
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
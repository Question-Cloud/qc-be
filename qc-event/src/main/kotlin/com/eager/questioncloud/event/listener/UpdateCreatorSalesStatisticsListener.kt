package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.creator.listener.UpdateCreatorSalesStatisticsHandler
import com.eager.questioncloud.event.annotation.IdempotentEvent
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateCreatorSalesStatisticsListener(
    private val updateCreatorSalesStatisticsHandler: UpdateCreatorSalesStatisticsHandler
) {
    @SqsListener("update-creator-sales-statistics.fifo")
    @IdempotentEvent
    fun handle(@Payload event: QuestionPaymentEvent) {
        updateCreatorSalesStatisticsHandler.updateCreatorStatistics(event)
    }
}
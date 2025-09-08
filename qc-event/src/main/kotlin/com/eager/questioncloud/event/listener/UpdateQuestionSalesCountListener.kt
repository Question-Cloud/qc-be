package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.question.handler.UpdateQuestionSalesCountHandler
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateQuestionSalesCountListener(
    private val updateQuestionSalesCountHandler: UpdateQuestionSalesCountHandler
) {
    @SqsListener("update-question-sales-count.fifo")
    @IdempotentEvent
    fun handle(@Payload event: QuestionPaymentEvent) {
        updateQuestionSalesCountHandler.updateSalesCount(event)
    }
}
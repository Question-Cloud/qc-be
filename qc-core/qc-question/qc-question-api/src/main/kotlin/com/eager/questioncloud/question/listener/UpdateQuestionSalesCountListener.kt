package com.eager.questioncloud.question.listener

import com.eager.questioncloud.common.event.QuestionPaymentEventPayload
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateQuestionSalesCountListener(
    private val questionMetadataRepository: QuestionMetadataRepository,
) {
    @SqsListener("update-question-sales-count.fifo")
    @IdempotentEvent
    fun updateSalesCount(@Payload event: QuestionPaymentEventPayload) {
        event.questionIds.forEach { questionMetadataRepository.increaseSales(it) }
    }
}

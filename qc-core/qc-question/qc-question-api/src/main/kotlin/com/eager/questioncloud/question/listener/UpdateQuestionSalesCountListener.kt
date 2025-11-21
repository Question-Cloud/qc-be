package com.eager.questioncloud.question.listener

import com.eager.questioncloud.common.event.IdempotentEvent
import com.eager.questioncloud.common.event.MessageListener
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.common.event.QueueListener
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
@QueueListener(queueName = "update-question-sales-count", type = QuestionPaymentEvent::class)
class UpdateQuestionSalesCountListener(
    private val questionMetadataRepository: QuestionMetadataRepository,
) : MessageListener<QuestionPaymentEvent> {
    
    @IdempotentEvent
    override fun onMessage(event: QuestionPaymentEvent) {
        event.questionIds.forEach { questionMetadataRepository.increaseSales(it) }
    }
}
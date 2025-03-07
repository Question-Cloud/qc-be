package com.eager.questioncloud.application.business.question.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.payment.model.QuestionOrderItem
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class QuestionStatisticsUpdater(
    private val questionRepository: QuestionRepository
) {
    @SqsListener("update-question-sales-count.fifo")
    fun updateSalesCount(@Payload event: QuestionPaymentEvent) {
        event.questionPayment
            .order
            .items
            .forEach(Consumer { item: QuestionOrderItem -> questionRepository.increaseQuestionCount(item.questionId) })
    }
}

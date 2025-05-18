package com.eager.questioncloud.application.listener.payment

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegisterListener(
    private val questionRepository: QuestionRepository,
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) {
    @SqsListener("question-payment-history-register.fifo")
    fun saveQuestionPaymentHistory(@Payload event: QuestionPaymentEvent) {
        val questions = questionRepository.findByQuestionIdIn(event.questionPayment.order.questionIds)
        questionPaymentHistoryRepository.save(create(event.questionPayment, questions))
    }
}

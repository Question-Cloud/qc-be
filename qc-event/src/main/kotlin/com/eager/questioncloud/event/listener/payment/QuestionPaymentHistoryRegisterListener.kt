package com.eager.questioncloud.event.listener.payment

import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.infrastructure.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
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
        val questions = questionRepository.findByQuestionIdIn(event.data.questionIds)
        questionPaymentHistoryRepository.save(
            QuestionPaymentHistory.create(
                event.data.orderId,
                event.data.buyerUserId,
                questions,
                event.data.questionPaymentCoupon,
                event.data.amount
            )
        )
    }
}

package com.eager.questioncloud.application.business.payment.question.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegister(
    private val questionRepository: QuestionRepository,
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) {
    @EventListener
    fun saveQuestionPaymentHistory(event: QuestionPaymentEvent) {
        val questions = questionRepository.findByQuestionIdIn(event.questionPayment.order.questionIds)
        questionPaymentHistoryRepository.save(create(event.questionPayment, questions))
    }
}

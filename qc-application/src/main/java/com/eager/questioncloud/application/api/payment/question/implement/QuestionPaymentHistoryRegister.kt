package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegister(
    private val questionRepository: QuestionRepository,
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) {
    fun saveQuestionPaymentHistory(questionPayment: QuestionPayment) {
        val questions = questionRepository.findByQuestionIdIn(questionPayment.order.questionIds)
        questionPaymentHistoryRepository.save(create(questionPayment, questions))
    }
}

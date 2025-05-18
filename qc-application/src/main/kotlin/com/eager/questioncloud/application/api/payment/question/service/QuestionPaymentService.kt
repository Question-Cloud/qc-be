package com.eager.questioncloud.application.api.payment.question.service

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentEventProcessor
import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentProcessor
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionPaymentService(
    private val questionPaymentProcessor: QuestionPaymentProcessor,
    private val questionPaymentEventProcessor: QuestionPaymentEventProcessor,
) {
    @Transactional
    fun payment(userId: Long, order: QuestionOrder, questionPaymentCoupon: QuestionPaymentCoupon?): QuestionPayment {
        val questionPayment = QuestionPayment.create(userId, questionPaymentCoupon, order)
        questionPaymentProcessor.payment(questionPayment)
        questionPaymentEventProcessor.saveEventLog(QuestionPaymentEvent.create(questionPayment))
        return questionPayment
    }
}

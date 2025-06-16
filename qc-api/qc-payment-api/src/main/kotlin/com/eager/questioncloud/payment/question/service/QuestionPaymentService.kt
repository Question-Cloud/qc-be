package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.event.model.QuestionPaymentEventData
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.question.implement.QuestionPaymentEventProcessor
import com.eager.questioncloud.payment.question.implement.QuestionPaymentProcessor
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
        questionPaymentEventProcessor.saveEventLog(
            QuestionPaymentEvent.create(
                QuestionPaymentEventData(
                    questionPayment.order.orderId,
                    questionPayment.userId,
                    questionPayment.order.questionIds,
                    questionPayment.amount,
                    questionPaymentCoupon
                )
            )
        )
        return questionPayment
    }
}

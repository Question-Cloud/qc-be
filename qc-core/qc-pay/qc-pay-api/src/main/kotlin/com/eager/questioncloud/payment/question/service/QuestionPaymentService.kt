package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.event.implement.EventPublisher
import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.event.model.QuestionPaymentEventCouponData
import com.eager.questioncloud.event.model.QuestionPaymentEventData
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.question.implement.QuestionPaymentProcessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionPaymentService(
    private val questionPaymentProcessor: QuestionPaymentProcessor,
    private val eventPublisher: EventPublisher,
) {
    @Transactional
    fun payment(userId: Long, order: QuestionOrder, questionPaymentCoupon: QuestionPaymentCoupon?): QuestionPayment {
        val questionPayment = QuestionPayment.create(userId, questionPaymentCoupon, order)
        questionPaymentProcessor.payment(questionPayment)
        eventPublisher.saveEventTicket(QuestionPaymentEvent.create(toEventData(questionPayment)))
        return questionPayment
    }
    
    private fun toEventData(questionPayment: QuestionPayment): QuestionPaymentEventData {
        val questionPaymentCoupon: QuestionPaymentCoupon? = questionPayment.questionPaymentCoupon
        return QuestionPaymentEventData(
            questionPayment.order.orderId,
            questionPayment.userId,
            questionPayment.order.questionIds,
            questionPayment.amount,
            questionPaymentCoupon?.let {
                QuestionPaymentEventCouponData(
                    questionPaymentCoupon.userCouponId,
                    questionPaymentCoupon.couponId,
                    questionPaymentCoupon.title,
                    questionPaymentCoupon.couponType.name,
                    questionPaymentCoupon.value
                )
            }
        )
    }
}

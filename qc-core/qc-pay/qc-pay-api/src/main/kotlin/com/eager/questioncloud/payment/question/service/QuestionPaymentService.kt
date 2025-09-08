package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.common.event.QuestionPaymentEventCouponData
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
        eventPublisher.publish(toEvent(questionPayment))
        return questionPayment
    }
    
    private fun toEvent(questionPayment: QuestionPayment): QuestionPaymentEvent {
        val questionPaymentCoupon: QuestionPaymentCoupon? = questionPayment.questionPaymentCoupon
        return QuestionPaymentEvent(
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

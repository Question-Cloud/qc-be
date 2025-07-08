package com.eager.questioncloud.pay.question.service

import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.event.model.QuestionPaymentEventCouponData
import com.eager.questioncloud.event.model.QuestionPaymentEventData
import com.eager.questioncloud.pay.question.implement.QuestionPaymentEventProcessor
import com.eager.questioncloud.pay.question.implement.QuestionPaymentProcessor
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
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
            )
        )
        return questionPayment
    }
}

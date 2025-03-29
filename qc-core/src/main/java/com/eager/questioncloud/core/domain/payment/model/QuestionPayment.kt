package com.eager.questioncloud.core.domain.payment.model

import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPayment(
    var order: QuestionOrder,
    var userId: Long,
    var questionPaymentCoupon: QuestionPaymentCoupon?,
    var amount: Int,
    var status: QuestionPaymentStatus = QuestionPaymentStatus.SUCCESS,
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun isUsedCoupon(): Boolean {
        return questionPaymentCoupon != null
    }

    fun applyCoupon() {
        amount = questionPaymentCoupon!!.calcDiscount(amount)
    }

    fun fail() {
        this.status = QuestionPaymentStatus.FAIL
    }

    companion object {
        fun create(
            userId: Long,
            questionPaymentCoupon: QuestionPaymentCoupon?,
            order: QuestionOrder
        ): QuestionPayment {
            return QuestionPayment(
                order = order,
                userId = userId,
                questionPaymentCoupon = questionPaymentCoupon,
                amount = order.calcAmount(),
            )
        }
    }
}

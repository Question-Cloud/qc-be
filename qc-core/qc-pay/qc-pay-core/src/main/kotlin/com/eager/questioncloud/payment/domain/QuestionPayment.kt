package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.event.CouponUsageInformation
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPayment(
    var order: QuestionOrder,
    var userId: Long,
    var questionPaymentCoupon: QuestionPaymentCoupon,
    var amount: Int,
    var status: QuestionPaymentStatus = QuestionPaymentStatus.SUCCESS,
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun applyCoupon() {
        amount = questionPaymentCoupon.calcDiscount(amount)
    }
    
    fun getCouponUsageInformation(): CouponUsageInformation {
        return CouponUsageInformation(
            questionPaymentCoupon.getCouponName(),
            order.calcAmount() - questionPaymentCoupon.calcDiscount(amount)
        )
    }
    
    companion object {
        fun payment(
            userId: Long,
            questionPaymentCoupon: QuestionPaymentCoupon,
            order: QuestionOrder
        ): QuestionPayment {
            val payment = QuestionPayment(
                order = order,
                userId = userId,
                questionPaymentCoupon = questionPaymentCoupon,
                amount = order.calcAmount(),
            )
            payment.applyCoupon()
            return payment
        }
    }
}
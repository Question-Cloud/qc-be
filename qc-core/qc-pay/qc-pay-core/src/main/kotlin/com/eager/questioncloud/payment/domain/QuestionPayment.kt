package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import java.time.LocalDateTime

class QuestionPayment(
    var paymentId: Long = 0,
    val userId: Long,
    val order: QuestionOrder,
    val paymentDiscount: MutableList<DiscountHistory> = mutableListOf(),
    val originalAmount: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    val orderId = order.orderId
    var realAmount: Int = order.totalPrice
    val orderDiscount: List<DiscountHistory>
        get() = order.orderDiscount
    
    companion object {
        fun create(
            userId: Long,
            order: QuestionOrder
        ): QuestionPayment {
            return QuestionPayment(
                order = order,
                userId = userId,
                originalAmount = order.totalOriginalPrice
            )
        }
    }
    
    fun stored(paymentId: Long) {
        this.paymentId = paymentId
    }
    
    fun applyPaymentCoupon(couponPolicy: CouponPolicy) {
        val discountAmount = couponPolicy.getDiscountAmount(realAmount)
        realAmount -= discountAmount
        
        if (realAmount < 0) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        paymentDiscount.add(
            DiscountHistory(
                couponType = couponPolicy.coupon.couponType,
                discountAmount = discountAmount,
                name = couponPolicy.getName(),
                sourceId = couponPolicy.getSourceId()
            )
        )
    }
}
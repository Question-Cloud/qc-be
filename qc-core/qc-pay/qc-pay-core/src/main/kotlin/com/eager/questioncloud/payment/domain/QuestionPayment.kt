package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.CouponType
import java.time.LocalDateTime

class QuestionPayment(
    var paymentId: Long = 0,
    val userId: Long,
    val order: QuestionOrder,
    val paymentDiscount: MutableList<DiscountHistory> = mutableListOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    val orderId = order.orderId
    val originalAmount: Int = order.totalOriginalPrice
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
            )
        }
    }
    
    fun stored(paymentId: Long) {
        this.paymentId = paymentId
    }
    
    fun applyPaymentCoupon(couponPolicy: CouponPolicy) {
        if (couponPolicy.couponInformation.couponType != CouponType.PAYMENT) throw CoreException(Error.WRONG_COUPON)
        
        val discountAmount = couponPolicy.getDiscountAmount(realAmount)
        realAmount -= discountAmount
        realAmount = realAmount.coerceAtLeast(0)
        
        paymentDiscount.add(
            DiscountHistory(
                couponType = couponPolicy.couponInformation.couponType,
                discountAmount = discountAmount,
                name = couponPolicy.getName(),
                sourceId = couponPolicy.getSourceId()
            )
        )
    }
}
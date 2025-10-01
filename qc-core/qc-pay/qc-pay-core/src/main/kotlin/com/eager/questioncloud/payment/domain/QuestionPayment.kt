package com.eager.questioncloud.payment.domain

import java.time.LocalDateTime

class QuestionPayment(
    var paymentId: Long = 0,
    val userId: Long,
    val order: QuestionOrder,
) {
    val orderId = order.orderId
    val createdAt: LocalDateTime = LocalDateTime.now()
    val appliedPaymentCoupons: MutableList<Coupon> = mutableListOf()
    var paymentDiscountAmount: Int = 0
    
    val originalAmount: Int
        get() = order.totalOriginalPrice
    
    val realAmount: Int
        get() = order.totalPrice - paymentDiscountAmount
    
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
    
    fun applyDiscount(discountAmount: Int) {
        paymentDiscountAmount += discountAmount
    }
    
    fun getOrderItem(questionId: Long): QuestionOrderItem {
        return order.getOrderItem(questionId)
    }
}
package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import java.time.LocalDateTime

class QuestionPayment(
    val order: QuestionOrder,
    val userId: Long,
    val discountHistory: MutableList<DiscountHistory> = mutableListOf(),
    val originalAmount: Int,
    var realAmount: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun applyDiscount(policy: Discountable) {
        val discountAmount = policy.getDiscountAmount(order.totalPriceAfterPromotions)
        realAmount -= discountAmount
        
        if (realAmount < 0) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        discountHistory.add(
            DiscountHistory(
                orderId = order.orderId,
                discountType = policy.getDiscountType(),
                discountAmount = discountAmount,
                name = policy.getName(),
                sourceId = policy.getSourceId()
            )
        )
    }
    
    companion object {
        fun create(
            userId: Long,
            order: QuestionOrder
        ): QuestionPayment {
            return QuestionPayment(
                order = order,
                userId = userId,
                originalAmount = order.totalOriginalPrice,
                realAmount = order.totalPriceAfterPromotions
            )
        }
    }
}
package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import io.hypersistence.tsid.TSID

class QuestionOrder(
    val orderId: String,
    val items: List<QuestionOrderItem>
) {
    val totalOriginalPrice: Int
        get() = items.sumOf { it.originalPrice }
    
    val totalPriceAfterPromotions: Int
        get() = items.sumOf { it.realPrice }
    
    val questionIds: List<Long>
        get() = items
            .map { obj: QuestionOrderItem -> obj.questionId }
    
    fun applyPromotion(promotion: Promotion) {
        val target = items.find { it.questionId == promotion.questionId }
        
        if (target == null) {
            throw CoreException(Error.PAYMENT_ERROR)
        }
        
        target.applyPromotion(promotion)
    }
    
    companion object {
        fun createOrder(orderItems: List<QuestionOrderItem>): QuestionOrder {
            val orderId = TSID.Factory.getTsid().toString()
            return QuestionOrder(orderId, orderItems)
        }
    }
}

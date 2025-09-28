package com.eager.questioncloud.payment.domain

import io.hypersistence.tsid.TSID

class QuestionOrder(
    var orderId: String,
    var items: List<QuestionOrderItem>
) {
    val totalOriginalPrice: Int
        get() = items.sumOf { it.originalPrice }
    
    val totalPriceAfterPromotions: Int
        get() = items.sumOf { it.realPrice }
    
    val questionIds: List<Long>
        get() = items
            .map { obj: QuestionOrderItem -> obj.questionId }
    
    companion object {
        fun createOrder(orderItems: List<QuestionOrderItem>): QuestionOrder {
            val orderId = TSID.Factory.getTsid().toString()
            return QuestionOrder(orderId, orderItems)
        }
    }
}

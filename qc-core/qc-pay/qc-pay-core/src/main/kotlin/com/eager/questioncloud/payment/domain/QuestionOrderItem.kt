package com.eager.questioncloud.payment.domain

class QuestionOrderItem(
    var id: Long = 0,
    var questionId: Long,
    var originalPrice: Int,
    val promotion: Discountable? = null,
) {
    val realPrice: Int
    val promotionDiscountAmount: Int
    
    init {
        val discountAmount = promotion?.getDiscountAmount(originalPrice) ?: 0
        realPrice = originalPrice - discountAmount
        promotionDiscountAmount = discountAmount
    }
}
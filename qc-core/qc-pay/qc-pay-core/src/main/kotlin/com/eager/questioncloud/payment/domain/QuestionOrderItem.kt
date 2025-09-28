package com.eager.questioncloud.payment.domain

class QuestionOrderItem(
    var id: Long = 0,
    var questionId: Long,
    var originalPrice: Int,
    var realPrice: Int = originalPrice,
    val promotion: Discountable? = null,
    var promotionDiscountAmount: Int = 0,
) {
    init {
        if (promotion != null) {
            val discountAmount = promotion.getDiscountAmount(originalPrice)
            realPrice -= discountAmount
            promotionDiscountAmount = discountAmount
        }
    }
}
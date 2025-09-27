package com.eager.questioncloud.payment.domain

class QuestionOrderItem(
    var id: Long = 0,
    var questionId: Long,
    var originalPrice: Int,
    var realPrice: Int = originalPrice,
    val promotion: Discountable? = null,
) {
    init {
        if (promotion != null) {
            realPrice -= promotion.getDiscountAmount(originalPrice)
        }
    }
}
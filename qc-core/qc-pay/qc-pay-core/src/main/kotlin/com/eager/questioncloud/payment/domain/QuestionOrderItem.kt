package com.eager.questioncloud.payment.domain

class QuestionOrderItem(
    val id: Long = 0,
    val questionId: Long,
    val originalPrice: Int,
    var realPrice: Int = originalPrice,
) {
    var promotionId: Long? = null
    var promotionName: String? = null
    var promotionDiscountAmount: Int = 0
    
    fun applyPromotion(promotion: Promotion) {
        val discountable = promotion.toDiscountable()
        promotionId = discountable.getSourceId()
        promotionName = discountable.getName()
        promotionDiscountAmount = discountable.getDiscountAmount(originalPrice)
        realPrice -= promotionDiscountAmount
    }
}
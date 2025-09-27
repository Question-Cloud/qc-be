package com.eager.questioncloud.payment.domain

class Promotion(
    val id: Long = 0,
    val questionId: Long,
    val promotionType: PromotionType,
    val title: String,
    val value: Int,
    val isActive: Boolean,
) {
    fun toDiscountable(): Discountable {
        return when (promotionType) {
            PromotionType.PERCENT -> PercentPromotion(id, title, value)
            PromotionType.FIXED -> FixedPromotion(id, title, value)
        }
    }
}
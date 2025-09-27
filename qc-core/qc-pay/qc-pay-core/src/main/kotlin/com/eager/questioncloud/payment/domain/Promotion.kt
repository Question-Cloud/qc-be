package com.eager.questioncloud.payment.domain

class Promotion(
    val id: Long = 0,
    val questionId: Long,
    val promotionType: PromotionType,
    val title: String,
    val value: Int,
    val isActive: Boolean,
) {
    fun toDiscountPolicy(): DiscountPolicy {
        return when (promotionType) {
            PromotionType.PERCENT -> PercentPromotionDiscount(id, title, value)
            PromotionType.FIXED -> FixedPromotionDiscount(id, title, value)
        }
    }
}
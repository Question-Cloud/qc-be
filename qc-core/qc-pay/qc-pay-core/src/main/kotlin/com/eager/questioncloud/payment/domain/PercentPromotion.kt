package com.eager.questioncloud.payment.domain

import kotlin.math.floor

class PercentPromotion(
    val promotionId: Long,
    val title: String,
    val value: Int,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originAmount * discountRate).toInt()
        return discountAmount
    }
    
    override fun getName(): String {
        return title
    }
    
    override fun getDiscountType(): DiscountType {
        return DiscountType.PROMOTION
    }
    
    override fun getSourceId(): Long {
        return promotionId
    }
}
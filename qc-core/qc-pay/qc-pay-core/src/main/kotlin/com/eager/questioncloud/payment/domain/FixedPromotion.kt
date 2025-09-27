package com.eager.questioncloud.payment.domain

class FixedPromotion(
    val promotionId: Long,
    val title: String,
    val value: Int,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        return value
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
    
    override fun getAppliedDiscountAmount(): Int {
        return value
    }
}
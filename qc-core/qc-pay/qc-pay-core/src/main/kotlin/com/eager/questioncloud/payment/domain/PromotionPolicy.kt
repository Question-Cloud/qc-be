package com.eager.questioncloud.payment.domain

class PromotionPolicy(
    val promotion: Promotion,
) : Discountable {
    override fun getDiscountAmount(originAmount: Int): Int {
        return originAmount - promotion.salePrice
    }
    
    override fun getName(): String {
        return promotion.title
    }
    
    override fun getSourceId(): Long {
        return promotion.id
    }
}
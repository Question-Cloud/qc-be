package com.eager.questioncloud.payment.domain

class NoDiscount : DiscountPolicy {
    override fun discount(originAmount: Int): Int {
        return originAmount
    }
    
    override fun getPolicyName(): String {
        return "할인 미적용"
    }
    
    override fun getDiscountValue(): Int {
        return 0
    }
}
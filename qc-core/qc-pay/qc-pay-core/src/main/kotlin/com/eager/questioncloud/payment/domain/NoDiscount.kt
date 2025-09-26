package com.eager.questioncloud.payment.domain

class NoDiscount : DiscountPolicy {
    override fun getDiscountAmount(originAmount: Int): Int {
        return 0
    }
    
    override fun getPolicyName(): String {
        return "할인 미적용"
    }
}
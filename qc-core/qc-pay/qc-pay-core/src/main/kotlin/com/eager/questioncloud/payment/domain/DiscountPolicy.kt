package com.eager.questioncloud.payment.domain

interface DiscountPolicy {
    fun getDiscountAmount(originAmount: Int): Int
    
    fun getPolicyName(): String
    
    fun getDiscountType(): DiscountType
    
    fun getSourceId(): Long
}
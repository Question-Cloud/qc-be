package com.eager.questioncloud.payment.domain

interface DiscountPolicy {
    fun discount(originAmount: Int): Int
    
    fun getPolicyName(): String
    
    fun getDiscountValue(): Int
}
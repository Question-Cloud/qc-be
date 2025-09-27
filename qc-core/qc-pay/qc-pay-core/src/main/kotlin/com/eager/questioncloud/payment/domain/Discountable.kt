package com.eager.questioncloud.payment.domain

interface Discountable {
    fun getDiscountAmount(originAmount: Int): Int
    
    fun getName(): String
    
    fun getDiscountType(): DiscountType
    
    fun getSourceId(): Long
    
    fun getAppliedDiscountAmount(): Int
}
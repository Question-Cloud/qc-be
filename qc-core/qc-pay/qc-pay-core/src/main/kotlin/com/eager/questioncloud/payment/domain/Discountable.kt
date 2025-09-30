package com.eager.questioncloud.payment.domain

interface Discountable {
    fun getDiscountAmount(originAmount: Int): Int
    
    fun getName(): String
    
    fun getSourceId(): Long
}
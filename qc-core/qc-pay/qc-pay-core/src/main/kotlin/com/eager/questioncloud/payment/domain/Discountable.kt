package com.eager.questioncloud.payment.domain

interface Discountable {
    fun getDiscountAmount(beforeAmount: Int): Int
    
    fun getName(): String
    
    fun getSourceId(): Long
}
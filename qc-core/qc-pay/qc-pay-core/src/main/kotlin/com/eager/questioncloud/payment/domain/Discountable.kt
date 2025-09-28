package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.DiscountType

interface Discountable {
    fun getDiscountAmount(originAmount: Int): Int
    
    fun getName(): String
    
    fun getDiscountType(): DiscountType
    
    fun getSourceId(): Long
}
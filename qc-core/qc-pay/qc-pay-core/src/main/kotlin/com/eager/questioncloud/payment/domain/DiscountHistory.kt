package com.eager.questioncloud.payment.domain

class DiscountHistory(
    val id: Long = 0,
    val orderId: String,
    val discountType: DiscountType,
    val appliedAmount: Int,
    val name: String,
    val sourceId: Long,
)
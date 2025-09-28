package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.DiscountType

data class DiscountHistory(
    val id: Long = 0,
    val orderId: String,
    val discountType: DiscountType,
    val discountAmount: Int,
    val name: String,
    val sourceId: Long,
)

data class SimpleDiscountHistory(
    val name: String,
    val discountAmount: Int,
)
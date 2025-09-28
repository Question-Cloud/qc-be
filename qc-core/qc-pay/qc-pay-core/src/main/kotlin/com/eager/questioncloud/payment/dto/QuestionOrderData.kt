package com.eager.questioncloud.payment.dto

data class QuestionOrderData(
    val orderItemId: Long,
    val orderId: String,
    val questionId: Long,
    val originalPrice: Int,
    val realPrice: Int,
    val promotionId: Long? = null,
    val promotionName: String? = null,
    val promotionDiscountAmount: Int,
)
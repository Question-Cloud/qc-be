package com.eager.questioncloud.payment.dto

data class QuestionPromotionData(
    val id: Long,
    val questionId: Long,
    val title: String,
    val salePrice: Int
)

package com.eager.questioncloud.payment.dto

data class QuestionPaymentData(
    val orderId: String,
    val userId: Long,
    val originalAmount: Int,
    val realAmount: Int,
)
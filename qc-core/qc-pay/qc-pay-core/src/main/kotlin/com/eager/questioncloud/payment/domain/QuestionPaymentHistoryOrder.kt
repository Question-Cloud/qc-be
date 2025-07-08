package com.eager.questioncloud.payment.domain

class QuestionPaymentHistoryOrder(
    val questionId: Long,
    val amount: Int,
    val title: String,
    val thumbnail: String,
    val creatorName: String, // TODO creatorName
    val subject: String,
    val mainCategory: String,
    val subCategory: String,
)
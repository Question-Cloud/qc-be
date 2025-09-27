package com.eager.questioncloud.payment.domain

class QuestionPaymentHistoryOrder(
    val questionId: Long,
    val originalPrice: Int,
    val realPrice: Int,
    val promotionName: String?,
    val appliedPromotionDiscountAmount: Int,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: String,
    val mainCategory: String,
    val subCategory: String,
)
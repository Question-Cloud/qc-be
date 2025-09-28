package com.eager.questioncloud.payment.domain

import java.time.LocalDateTime

data class QuestionPaymentHistory(
    val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrder>,
    val discountInformation: List<SimpleDiscountHistory>,
    val originalAmount: Int,
    val realAmount: Int,
    val createdAt: LocalDateTime,
) {
    
    companion object {
        fun create(
            orderId: String,
            userId: Long,
            orders: List<QuestionPaymentHistoryOrder>,
            discountInformation: List<SimpleDiscountHistory>,
            originalAmount: Int,
            realAmount: Int,
        ): QuestionPaymentHistory {
            return QuestionPaymentHistory(
                orderId,
                userId,
                orders,
                discountInformation,
                originalAmount,
                realAmount,
                LocalDateTime.now(),
            )
        }
    }
}

data class QuestionPaymentHistoryOrder(
    val questionId: Long,
    val originalPrice: Int,
    val realPrice: Int,
    val promotionName: String?,
    val promotionDiscountAmount: Int,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: String,
    val mainCategory: String,
    val subCategory: String,
)
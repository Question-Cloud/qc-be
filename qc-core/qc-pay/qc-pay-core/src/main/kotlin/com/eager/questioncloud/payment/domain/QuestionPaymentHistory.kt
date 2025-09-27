package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPaymentHistory(
    val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrder>,
    val discountInformation: List<DiscountInformation>,
    val originalAmount: Int,
    val realAmount: Int,
    val status: QuestionPaymentStatus,
    val createdAt: LocalDateTime,
) {
    
    companion object {
        fun create(
            orderId: String,
            userId: Long,
            orders: List<QuestionPaymentHistoryOrder>,
            discountInformation: List<DiscountInformation>,
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
                QuestionPaymentStatus.SUCCESS,
                LocalDateTime.now(),
            )
        }
    }
}

class DiscountInformation(
    val title: String,
    val value: Int,
)
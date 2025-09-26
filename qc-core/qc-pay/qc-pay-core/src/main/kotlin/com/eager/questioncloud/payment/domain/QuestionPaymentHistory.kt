package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.event.DiscountInformation
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPaymentHistory(
    val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrder>,
    val discountInformation: List<DiscountInformation>,
    val amount: Int,
    val status: QuestionPaymentStatus,
    val createdAt: LocalDateTime,
) {
    
    companion object {
        fun create(
            orderId: String,
            userId: Long,
            orders: List<QuestionPaymentHistoryOrder>,
            discountInformation: List<DiscountInformation>,
            amount: Int,
        ): QuestionPaymentHistory {
            return QuestionPaymentHistory(
                orderId,
                userId,
                orders,
                discountInformation,
                amount,
                QuestionPaymentStatus.SUCCESS,
                LocalDateTime.now(),
            )
        }
    }
}

package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPaymentHistory(
    val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrder>,
    val coupon: QuestionPaymentCoupon?,
    val amount: Int,
    val isUsedCoupon: Boolean,
    val status: QuestionPaymentStatus,
    val createdAt: LocalDateTime,
) {

    companion object {
        fun create(
            orderId: String,
            userId: Long,
            orders: List<QuestionPaymentHistoryOrder>,
            questionPaymentCoupon: QuestionPaymentCoupon?,
            amount: Int,
        ): QuestionPaymentHistory {
            return QuestionPaymentHistory(
                orderId,
                userId,
                orders,
                questionPaymentCoupon,
                amount,
                questionPaymentCoupon != null,
                QuestionPaymentStatus.SUCCESS,
                LocalDateTime.now(),
            )
        }
    }
}

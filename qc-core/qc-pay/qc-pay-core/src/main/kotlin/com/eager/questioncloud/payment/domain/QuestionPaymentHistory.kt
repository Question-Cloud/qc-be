package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.event.CouponUsageInformation
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPaymentHistory(
    val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrder>,
    val coupon: CouponUsageInformation,
    val amount: Int,
    val status: QuestionPaymentStatus,
    val createdAt: LocalDateTime,
) {
    
    companion object {
        fun create(
            orderId: String,
            userId: Long,
            orders: List<QuestionPaymentHistoryOrder>,
            coupon: CouponUsageInformation,
            amount: Int,
        ): QuestionPaymentHistory {
            return QuestionPaymentHistory(
                orderId,
                userId,
                orders,
                coupon,
                amount,
                QuestionPaymentStatus.SUCCESS,
                LocalDateTime.now(),
            )
        }
    }
}

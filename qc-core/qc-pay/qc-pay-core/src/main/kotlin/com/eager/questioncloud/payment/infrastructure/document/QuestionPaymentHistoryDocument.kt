package com.eager.questioncloud.payment.infrastructure.document

import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "question_payment_history")
class QuestionPaymentHistoryDocument(
    @Id
    val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrder>,
    val coupon: QuestionPaymentCoupon?,
    val amount: Int,
    val isUsedCoupon: Boolean,
    val status: QuestionPaymentStatus,
    val createdAt: LocalDateTime,
) {
    fun toModel(): QuestionPaymentHistory {
        return QuestionPaymentHistory(
            orderId,
            userId,
            orders,
            coupon,
            amount,
            isUsedCoupon,
            status,
            createdAt
        )
    }

    companion object {
        fun from(questionPaymentHistory: QuestionPaymentHistory): QuestionPaymentHistoryDocument {
            return QuestionPaymentHistoryDocument(
                questionPaymentHistory.orderId,
                questionPaymentHistory.userId,
                questionPaymentHistory.orders,
                questionPaymentHistory.coupon,
                questionPaymentHistory.amount,
                questionPaymentHistory.isUsedCoupon,
                questionPaymentHistory.status,
                questionPaymentHistory.createdAt,
            )
        }
    }
}

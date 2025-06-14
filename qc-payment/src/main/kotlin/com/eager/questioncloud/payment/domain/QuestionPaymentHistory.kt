package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import com.eager.questioncloud.question.dto.QuestionInformation
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
        @JvmStatic
        fun create(
            questionPayment: QuestionPayment,
            questions: List<QuestionInformation>
        ): QuestionPaymentHistory {
            val orders = questions.stream()
                .map { question: QuestionInformation ->
                    QuestionPaymentHistoryOrder.from(question)
                }
                .toList()

            return QuestionPaymentHistory(
                questionPayment.order.orderId,
                questionPayment.userId,
                orders,
                questionPayment.questionPaymentCoupon,
                questionPayment.amount,
                questionPayment.isUsedCoupon(),
                questionPayment.status,
                questionPayment.createdAt,
            )
        }
    }
}

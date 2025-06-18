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
        fun create(
            orderId: String,
            userId: Long,
            questions: List<QuestionInformation>,
            questionPaymentCoupon: QuestionPaymentCoupon?,
            amount: Int,
        ): QuestionPaymentHistory {
            val orders = questions.stream()
                .map { question: QuestionInformation ->
                    QuestionPaymentHistoryOrder.from(question)
                }
                .toList()

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

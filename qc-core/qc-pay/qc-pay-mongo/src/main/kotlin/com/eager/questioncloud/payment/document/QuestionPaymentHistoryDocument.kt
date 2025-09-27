package com.eager.questioncloud.payment.document

import com.eager.questioncloud.payment.domain.DiscountInformation
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
    val discountInformation: List<DiscountInformation>,
    val originalAmount: Int,
    val realAmount: Int,
    val status: QuestionPaymentStatus,
    val createdAt: LocalDateTime,
) {
    fun toModel(): QuestionPaymentHistory {
        return QuestionPaymentHistory(
            orderId,
            userId,
            orders,
            discountInformation,
            originalAmount,
            realAmount,
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
                questionPaymentHistory.discountInformation,
                questionPaymentHistory.originalAmount,
                questionPaymentHistory.realAmount,
                questionPaymentHistory.status,
                questionPaymentHistory.createdAt,
            )
        }
    }
}

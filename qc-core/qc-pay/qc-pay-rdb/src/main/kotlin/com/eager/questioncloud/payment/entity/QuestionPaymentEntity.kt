package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.QuestionPayment
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question_payment")
class QuestionPaymentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val paymentId: Long = 0,
    @Column val orderId: String,
    @Column val userId: Long,
    @Column val realAmount: Int,
    @Column val originalAmount: Int,
    @Column val createdAt: LocalDateTime,
) {
    companion object {
        fun createNewEntity(questionPayment: QuestionPayment): QuestionPaymentEntity {
            return QuestionPaymentEntity(
                orderId = questionPayment.order.orderId,
                userId = questionPayment.userId,
                realAmount = questionPayment.realAmount,
                originalAmount = questionPayment.originalAmount,
                createdAt = questionPayment.createdAt,
            )
        }
    }
}

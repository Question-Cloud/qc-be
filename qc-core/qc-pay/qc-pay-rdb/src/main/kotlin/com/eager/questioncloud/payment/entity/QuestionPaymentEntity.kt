package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.common.entity.BaseCustomIdEntity
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question_payment")
class QuestionPaymentEntity(
    @Id val orderId: String,
    @Column val userId: Long,
    @Column val realAmount: Int,
    @Column val originalAmount: Int,
    @Enumerated(EnumType.STRING) @Column val status: QuestionPaymentStatus,
    @Column val createdAt: LocalDateTime,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    companion object {
        fun createNewEntity(questionPayment: QuestionPayment): QuestionPaymentEntity {
            return QuestionPaymentEntity(
                questionPayment.order.orderId,
                questionPayment.userId,
                questionPayment.realAmount,
                questionPayment.originalAmount,
                questionPayment.status,
                questionPayment.createdAt,
                true
            )
        }
    }
    
    override fun getId(): String {
        return orderId
    }
}

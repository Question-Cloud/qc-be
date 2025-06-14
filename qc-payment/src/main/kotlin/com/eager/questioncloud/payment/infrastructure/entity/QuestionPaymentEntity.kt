package com.eager.questioncloud.payment.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question_payment")
class QuestionPaymentEntity(
    @Id var orderId: String,
    @Column var userId: Long,
    @Column var userCouponId: Long?,
    @Column var amount: Int,
    @Enumerated(EnumType.STRING) @Column var status: QuestionPaymentStatus,
    @Column var createdAt: LocalDateTime,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    companion object {
        fun createNewEntity(questionPayment: QuestionPayment): QuestionPaymentEntity {
            return QuestionPaymentEntity(
                questionPayment.order.orderId,
                questionPayment.userId,
                questionPayment.questionPaymentCoupon?.userCouponId,
                questionPayment.amount,
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

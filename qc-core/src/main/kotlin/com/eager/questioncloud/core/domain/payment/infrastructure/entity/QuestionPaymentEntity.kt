package com.eager.questioncloud.core.domain.payment.infrastructure.entity

import com.eager.questioncloud.core.common.BaseCustomIdEntity
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
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
    @Column var createdAt: LocalDateTime
) : BaseCustomIdEntity<String>() {
    companion object {
        fun from(questionPayment: QuestionPayment): QuestionPaymentEntity {
            return QuestionPaymentEntity(
                questionPayment.order.orderId,
                questionPayment.userId,
                questionPayment.questionPaymentCoupon?.userCouponId,
                questionPayment.amount,
                questionPayment.status,
                questionPayment.createdAt
            )
        }
    }

    override fun getId(): String {
        return orderId
    }
}

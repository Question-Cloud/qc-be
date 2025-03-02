package com.eager.questioncloud.core.domain.point.infrastructure.entity

import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "charge_point_payment")
class ChargePointPaymentEntity private constructor(
    @Id var paymentId: String,
    @Column var userId: Long,
    @Column var receiptUrl: String?,
    @Enumerated(EnumType.STRING) @Column var chargePointType: ChargePointType,
    @Enumerated(EnumType.STRING) @Column var chargePointPaymentStatus: ChargePointPaymentStatus,
    @Column var createdAt: LocalDateTime,
    @Column var paidAt: LocalDateTime?
) {
    fun toModel(): ChargePointPayment {
        return ChargePointPayment(
            paymentId,
            userId,
            receiptUrl,
            chargePointType,
            chargePointPaymentStatus,
            createdAt,
            paidAt
        )
    }

    companion object {
        @JvmStatic
        fun from(chargePointPayment: ChargePointPayment): ChargePointPaymentEntity {
            return ChargePointPaymentEntity(
                chargePointPayment.paymentId,
                chargePointPayment.userId,
                chargePointPayment.receiptUrl,
                chargePointPayment.chargePointType,
                chargePointPayment.chargePointPaymentStatus,
                chargePointPayment.createdAt,
                chargePointPayment.paidAt
            )
        }
    }
}

package com.eager.questioncloud.core.domain.point.infrastructure.entity

import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "charge_point_payment")
class ChargePointPaymentEntity private constructor(
    @Id var orderId: String,
    @Column var paymentId: String? = null,
    @Column var userId: Long,
    @Enumerated(EnumType.STRING) @Column var chargePointType: ChargePointType,
    @Enumerated(EnumType.STRING) @Column var chargePointPaymentStatus: ChargePointPaymentStatus,
    @Column var createdAt: LocalDateTime,
    @Column var requestAt: LocalDateTime?
) {
    fun toModel(): ChargePointPayment {
        return ChargePointPayment(
            orderId,
            paymentId,
            userId,
            chargePointType,
            chargePointPaymentStatus,
            createdAt,
            requestAt
        )
    }

    companion object {
        @JvmStatic
        fun from(chargePointPayment: ChargePointPayment): ChargePointPaymentEntity {
            return ChargePointPaymentEntity(
                chargePointPayment.orderId,
                chargePointPayment.paymentId,
                chargePointPayment.userId,
                chargePointPayment.chargePointType,
                chargePointPayment.chargePointPaymentStatus,
                chargePointPayment.createdAt,
                chargePointPayment.requestAt
            )
        }
    }
}

package com.eager.questioncloud.point.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "charge_point_payment")
class ChargePointPaymentEntity(
    @Id var orderId: String,
    @Column var paymentId: String? = null,
    @Column var userId: Long,
    @Enumerated(EnumType.STRING) @Column var chargePointType: ChargePointType,
    @Enumerated(EnumType.STRING) @Column var chargePointPaymentStatus: ChargePointPaymentStatus,
    @Column var createdAt: LocalDateTime,
    @Column var requestAt: LocalDateTime?,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
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
        fun createNewEntity(chargePointPayment: ChargePointPayment): ChargePointPaymentEntity {
            return ChargePointPaymentEntity(
                chargePointPayment.orderId,
                chargePointPayment.paymentId,
                chargePointPayment.userId,
                chargePointPayment.chargePointType,
                chargePointPayment.chargePointPaymentStatus,
                chargePointPayment.createdAt,
                chargePointPayment.requestAt,
                true
            )
        }

        fun fromExisting(chargePointPayment: ChargePointPayment): ChargePointPaymentEntity {
            return ChargePointPaymentEntity(
                chargePointPayment.orderId,
                chargePointPayment.paymentId,
                chargePointPayment.userId,
                chargePointPayment.chargePointType,
                chargePointPayment.chargePointPaymentStatus,
                chargePointPayment.createdAt,
                chargePointPayment.requestAt,
                false
            )
        }
    }

    override fun getId(): String {
        return orderId
    }
}

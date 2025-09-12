package com.eager.questioncloud.point.entity

import com.eager.questioncloud.common.entity.BaseCustomIdEntity
import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "charge_point_payment_idempotent_info")
class ChargePointPaymentIdempotentInfoEntity(
    @Id val orderId: String,
    @Column val paymentId: String,
    @Enumerated(EnumType.STRING) @Column val chargePointPaymentStatus: ChargePointPaymentStatus,
    @Column val createdAt: LocalDateTime = LocalDateTime.now(),
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    override fun getId(): String {
        return orderId
    }
    
    companion object {
        fun createNewEntity(chargePointPaymentIdempotentInfo: ChargePointPaymentIdempotentInfo): ChargePointPaymentIdempotentInfoEntity {
            return ChargePointPaymentIdempotentInfoEntity(
                chargePointPaymentIdempotentInfo.orderId,
                chargePointPaymentIdempotentInfo.paymentId,
                chargePointPaymentIdempotentInfo.chargePointPaymentStatus,
                chargePointPaymentIdempotentInfo.createdAt,
                true
            )
        }
        
        fun fromExisting(chargePointPaymentIdempotentInfo: ChargePointPaymentIdempotentInfo): ChargePointPaymentIdempotentInfoEntity {
            return ChargePointPaymentIdempotentInfoEntity(
                chargePointPaymentIdempotentInfo.orderId,
                chargePointPaymentIdempotentInfo.paymentId,
                chargePointPaymentIdempotentInfo.chargePointPaymentStatus,
                chargePointPaymentIdempotentInfo.createdAt,
                false
            )
        }
    }
    
    fun toModel(): ChargePointPaymentIdempotentInfo {
        return ChargePointPaymentIdempotentInfo(
            orderId,
            paymentId,
            chargePointPaymentStatus,
            createdAt,
        )
    }
}
package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import org.springframework.stereotype.Repository

@Repository
class ChargePointPaymentIdempotentInfoEntityImpl(
    private val chargePointPaymentIdempotentInfoJpaRepository: ChargePointPaymentIdempotentInfoJpaRepository
) : ChargePointPaymentIdempotentInfoRepository {
    override fun insert(chargePointPaymentIdempotentInfo: ChargePointPaymentIdempotentInfo): Boolean {
        return chargePointPaymentIdempotentInfoJpaRepository.insert(
            chargePointPaymentIdempotentInfo.orderId,
            chargePointPaymentIdempotentInfo.paymentId,
            chargePointPaymentIdempotentInfo.chargePointPaymentStatus.name
        ) == 1
    }
    
    override fun findByOrderId(orderId: String): ChargePointPaymentIdempotentInfo? {
        val result = chargePointPaymentIdempotentInfoJpaRepository.findById(orderId)
        
        if (result.isPresent) {
            return result.get().toModel()
        }
        
        return null
    }
}
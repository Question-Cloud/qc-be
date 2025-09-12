package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.entity.ChargePointPaymentIdempotentInfoEntity
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository

@Repository
class ChargePointPaymentIdempotentInfoEntityImpl(
    private val chargePointPaymentIdempotentInfoJpaRepository: ChargePointPaymentIdempotentInfoJpaRepository
) : ChargePointPaymentIdempotentInfoRepository {
    override fun save(chargePointPaymentIdempotentInfo: ChargePointPaymentIdempotentInfo): Boolean {
        try {
            chargePointPaymentIdempotentInfoJpaRepository.save(
                ChargePointPaymentIdempotentInfoEntity.createNewEntity(
                    chargePointPaymentIdempotentInfo
                )
            )
            return true
        } catch (_: DataIntegrityViolationException) {
            return false
        }
    }
    
    override fun findByOrderId(orderId: String): ChargePointPaymentIdempotentInfo? {
        val result = chargePointPaymentIdempotentInfoJpaRepository.findById(orderId)
        
        if (result.isPresent) {
            return result.get().toModel()
        }
        
        return null
    }
}
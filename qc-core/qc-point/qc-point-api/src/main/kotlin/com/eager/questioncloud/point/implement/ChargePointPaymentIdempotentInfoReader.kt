package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.repository.ChargePointPaymentIdempotentInfoRepository
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentIdempotentInfoReader(
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository
) {
    fun getOrderId(orderId: String): ChargePointPaymentIdempotentInfo? {
        return chargePointPaymentIdempotentInfoRepository.findByOrderId(orderId)
    }
}
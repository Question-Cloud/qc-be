package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.repository.ChargePointPaymentIdempotentInfoRepository
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentFailHandler(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository
) {
    @Transactional
    fun fail(orderId: String, paymentId: String) {
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(orderId)
        val idempotentInfo = ChargePointPaymentIdempotentInfo(
            orderId = orderId,
            paymentId = paymentId,
            chargePointPaymentStatus = ChargePointPaymentStatus.FAILED
        )
        
        if (chargePointPaymentIdempotentInfoRepository.insert(idempotentInfo)) {
            chargePointPayment.fail()
            chargePointPaymentRepository.update(chargePointPayment)
        }
    }
}
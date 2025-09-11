package com.eager.questioncloud.point.handler

import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.point.implement.UserPointManager
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FailChargePointPaymentHandler(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val userPointManager: UserPointManager
) {
    
    @Transactional
    fun failHandler(event: FailChargePointPaymentMessagePayload) {
        val chargePointPayment = chargePointPaymentRepository.findByOrderIdWithLock(event.orderId)
        
        if (chargePointPayment.recovery()) {
            userPointManager.chargePoint(chargePointPayment.userId, chargePointPayment.chargePointType.amount)
            chargePointPaymentRepository.update(chargePointPayment)
        }
    }
}

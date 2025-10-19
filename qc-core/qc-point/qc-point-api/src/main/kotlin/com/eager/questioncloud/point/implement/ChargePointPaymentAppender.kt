package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentAppender(
    private val chargePointPaymentRepository: ChargePointPaymentRepository
) {
    fun createOrder(userId: Long, chargePointType: ChargePointType): ChargePointPayment {
        return chargePointPaymentRepository.save(ChargePointPayment.createOrder(userId, chargePointType))
    }
}
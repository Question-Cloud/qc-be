package com.eager.questioncloud.point.service

import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.point.implement.ChargePointPaymentPostProcessor
import com.eager.questioncloud.point.implement.ChargePointPaymentPreparer
import com.eager.questioncloud.point.infrastructure.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentService(
    private val chargePointPaymentPreparer: ChargePointPaymentPreparer,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
    private val chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor
) {
    fun createOrder(userId: Long, chargePointType: ChargePointType): String {
        val order = chargePointPaymentRepository.save(ChargePointPayment.createOrder(userId, chargePointType))
        return order.orderId
    }
    
    fun approvePayment(orderId: String) {
        val pgPayment = chargePointPaymentPGProcessor.getPayment(orderId)
        val chargePointPayment = chargePointPaymentPreparer.prepare(pgPayment)
        chargePointPaymentPGProcessor.confirm(
            pgPayment.paymentId,
            pgPayment.orderId,
            chargePointPayment.chargePointType.amount
        )
        chargePointPaymentPostProcessor.chargeUserPoint(chargePointPayment)
    }
    
    fun isCompletePayment(userId: Long, orderId: String): Boolean {
        return chargePointPaymentRepository.isCompletedPayment(userId, orderId)
    }
}

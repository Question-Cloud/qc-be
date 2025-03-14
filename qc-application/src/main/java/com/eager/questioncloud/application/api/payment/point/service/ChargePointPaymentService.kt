package com.eager.questioncloud.application.api.payment.point.service

import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentApprover
import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentPostProcessor
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentService(
    private val chargePointPaymentApprover: ChargePointPaymentApprover,
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
        val chargePointPayment = chargePointPaymentApprover.approve(pgPayment)
        chargePointPaymentPGProcessor.confirm(
            pgPayment.paymentId,
            pgPayment.orderId,
            chargePointPayment.chargePointType.amount
        )
        chargePointPaymentPostProcessor.chargeUserPoint(chargePointPayment)
    }

    fun isCompletePayment(userId: Long, paymentId: String): Boolean {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId)
    }
}

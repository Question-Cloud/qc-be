package com.eager.questioncloud.application.business.payment.point.service

import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentApprover
import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentPostProcessor
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.pg.dto.PGPayment
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentService(
    private val chargePointPaymentApprover: ChargePointPaymentApprover,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
) {
    fun createOrder(chargePointPayment: ChargePointPayment) {
        chargePointPaymentRepository.save(chargePointPayment)
    }

    fun approvePayment(pgPayment: PGPayment) {
        val chargePointPayment = chargePointPaymentApprover.approve(pgPayment)
        chargePointPaymentPostProcessor.chargeUserPoint(chargePointPayment)
    }

    fun isCompletePayment(userId: Long, paymentId: String): Boolean {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId)
    }
}

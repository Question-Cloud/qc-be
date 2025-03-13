package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.pg.dto.PGPayment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentApprover(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
) {
    @Transactional
    fun approve(pgPayment: PGPayment): ChargePointPayment {
        val chargePointPayment = chargePointPaymentRepository.findByOrderIdWithLock(pgPayment.orderId)
        chargePointPayment.validatePayment(pgPayment.amount)
        chargePointPayment.approve(pgPayment.paymentId)
        return chargePointPaymentRepository.save(chargePointPayment)
    }
}

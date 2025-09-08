package com.eager.questioncloud.point.implement

import com.eager.questioncloud.pg.model.PGPayment
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentPreparer(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
) {
    @Transactional
    fun prepare(pgPayment: PGPayment): ChargePointPayment {
        val chargePointPayment = chargePointPaymentRepository.findByOrderIdWithLock(pgPayment.orderId)
        chargePointPayment.validatePayment(pgPayment.amount)
        chargePointPayment.prepare(pgPayment.paymentId)
        return chargePointPaymentRepository.update(chargePointPayment)
    }
}

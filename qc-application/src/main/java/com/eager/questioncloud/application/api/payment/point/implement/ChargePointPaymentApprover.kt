package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.pg.dto.PGPayment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentApprover(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val failChargePointPaymentEventProcessor: FailChargePointPaymentEventProcessor,
) {
    @Transactional
    fun approve(pgPayment: PGPayment): ChargePointPayment {
        try {
            val chargePointPayment = chargePointPaymentRepository.findByOrderIdWithLock(pgPayment.orderId)
            chargePointPayment.validatePayment(pgPayment.amount)
            chargePointPayment.approve(pgPayment.paymentId)
            return chargePointPaymentRepository.save(chargePointPayment)
        } catch (coreException: CoreException) {
            throw coreException
        } catch (unknownException: Exception) {
            failChargePointPaymentEventProcessor.publishEvent(
                FailChargePointPaymentEvent(
                    pgPayment.orderId,
                    pgPayment.paymentId
                )
            )
            throw unknownException
        }
    }
}

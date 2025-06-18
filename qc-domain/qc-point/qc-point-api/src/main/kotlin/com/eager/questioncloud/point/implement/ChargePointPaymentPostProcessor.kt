package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.event.model.FailChargePointPaymentEvent
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.infrastructure.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentPostProcessor(
    private val userPointManager: UserPointManager,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val failChargePointPaymentEventProcessor: FailChargePointPaymentEventProcessor,
) {
    @Transactional
    fun chargeUserPoint(chargePointPayment: ChargePointPayment) {
        runCatching {
            userPointManager.chargePoint(chargePointPayment.userId, chargePointPayment.chargePointType.amount)
            chargePointPayment.charge()
            chargePointPaymentRepository.update(chargePointPayment)
        }.onFailure {
            failChargePointPaymentEventProcessor.publishEvent(FailChargePointPaymentEvent.create(chargePointPayment.orderId))
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}

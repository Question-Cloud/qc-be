package com.eager.questioncloud.application.listener.payment

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentListener(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor
) {
    @SqsListener("fail-charge-point-payment.fifo")
    fun failHandler(@Payload event: FailChargePointPaymentEvent) {
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(event.orderId)
        cancelChargePointPayment(chargePointPayment)
        cancelPG(chargePointPayment)
    }

    private fun cancelChargePointPayment(chargePointPayment: ChargePointPayment) {
        chargePointPayment.cancel()
        chargePointPaymentRepository.update(chargePointPayment)
    }

    private fun cancelPG(chargePointPayment: ChargePointPayment) {
        runCatching {
            chargePointPaymentPGProcessor.cancel(
                chargePointPayment.paymentId!!,
                chargePointPayment.chargePointType.amount
            )
        }.onFailure {
            failCancel(chargePointPayment)
        }
    }

    private fun failCancel(chargePointPayment: ChargePointPayment) {
        chargePointPayment.failCancel()
        chargePointPaymentRepository.update(chargePointPayment)
    }
}

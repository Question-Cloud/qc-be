package com.eager.questioncloud.point.listener

import com.eager.questioncloud.event.model.FailChargePointPaymentEvent
import com.eager.questioncloud.pg.toss.TossPaymentAPI
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.infrastructure.repository.ChargePointPaymentRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentListener(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val tossPaymentAPI: TossPaymentAPI
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
            tossPaymentAPI.cancel(
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

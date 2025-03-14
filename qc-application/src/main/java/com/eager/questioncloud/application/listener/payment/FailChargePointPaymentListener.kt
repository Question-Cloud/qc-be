package com.eager.questioncloud.application.listener.payment

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
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
        chargePointPayment.cancel()
        chargePointPaymentRepository.save(chargePointPayment)

        chargePointPaymentPGProcessor.cancel(chargePointPayment.paymentId!!, chargePointPayment.chargePointType.amount)
    }
}

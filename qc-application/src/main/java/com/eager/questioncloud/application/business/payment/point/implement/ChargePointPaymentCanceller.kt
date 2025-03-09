package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.business.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentCanceller(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val pgPaymentProcessor: PGPaymentProcessor
) {
    @SqsListener("cancel-charge-point-payment.fifo")
    fun failHandler(@Payload event: FailChargePointPaymentEvent) {
        val chargePointPayment = chargePointPaymentRepository.findByPaymentId(event.paymentId)

        pgPaymentProcessor.cancel(chargePointPayment.paymentId)

        chargePointPayment.cancel()
        chargePointPaymentRepository.save(chargePointPayment)
    }
}

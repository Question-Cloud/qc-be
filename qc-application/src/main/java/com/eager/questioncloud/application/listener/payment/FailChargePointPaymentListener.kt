package com.eager.questioncloud.application.listener.payment

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FailChargePointPaymentListener(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val pgPaymentProcessor: PGPaymentProcessor
) {
    @SqsListener("fail-charge-point-payment.fifo")
    @Transactional
    fun failHandler(@Payload event: FailChargePointPaymentEvent) {
        val chargePointPayment = chargePointPaymentRepository.findByOrderIdWithLock(event.orderId)
        
        chargePointPayment.cancel()
        chargePointPaymentRepository.save(chargePointPayment)

        pgPaymentProcessor.cancel(chargePointPayment.paymentId!!, chargePointPayment.chargePointType.amount)
    }
}

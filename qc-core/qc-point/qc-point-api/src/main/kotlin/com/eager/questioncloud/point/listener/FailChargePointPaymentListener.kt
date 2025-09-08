package com.eager.questioncloud.point.listener

import com.eager.questioncloud.event.model.FailChargePointPaymentMessage
import com.eager.questioncloud.pg.PaymentAPI
import com.eager.questioncloud.pg.model.PGPayment
import com.eager.questioncloud.pg.toss.PaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentListener(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val paymentAPI: PaymentAPI,
) {
    @SqsListener("fail-charge-point-payment.fifo")
    fun failHandler(@Payload event: FailChargePointPaymentMessage) {
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(event.orderId)
        val pgPayment = PGPayment(
            chargePointPayment.paymentId!!,
            chargePointPayment.orderId,
            chargePointPayment.chargePointType.amount,
            PaymentStatus.DONE
        )
        paymentAPI.cancel(pgPayment)
        cancelChargePointPayment(chargePointPayment)
    }
    
    private fun cancelChargePointPayment(chargePointPayment: ChargePointPayment) {
        chargePointPayment.cancel()
        chargePointPaymentRepository.update(chargePointPayment)
    }
}

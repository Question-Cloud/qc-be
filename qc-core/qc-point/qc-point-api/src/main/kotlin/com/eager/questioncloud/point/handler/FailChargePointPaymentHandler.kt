package com.eager.questioncloud.point.handler

import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.common.pg.PaymentAPI
import com.eager.questioncloud.common.pg.domain.PGPayment
import com.eager.questioncloud.common.pg.domain.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentHandler(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val paymentAPI: PaymentAPI,
) {
    fun failHandler(event: FailChargePointPaymentMessagePayload) {
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(event.orderId)
        val pgPayment = PGPayment(
            chargePointPayment.paymentId!!,
            chargePointPayment.orderId,
            chargePointPayment.chargePointType.amount,
            PGPaymentStatus.CANCELED
        )
        paymentAPI.cancel(pgPayment)
        cancelChargePointPayment(chargePointPayment)
    }
    
    private fun cancelChargePointPayment(chargePointPayment: ChargePointPayment) {
        chargePointPayment.cancel()
        chargePointPaymentRepository.update(chargePointPayment)
    }
}

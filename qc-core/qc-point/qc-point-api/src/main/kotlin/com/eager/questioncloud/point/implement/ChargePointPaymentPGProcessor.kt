package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pg.PaymentAPI
import com.eager.questioncloud.common.pg.domain.PGPayment
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentPGProcessor(
    private val paymentAPI: PaymentAPI,
) {
    fun getPayment(orderId: String): PGPayment {
        return paymentAPI.getPayment(orderId)
    }
    
    @Retryable(maxAttempts = 5)
    fun confirm(pgPayment: PGPayment) {
        paymentAPI.confirm(pgPayment)
    }
}

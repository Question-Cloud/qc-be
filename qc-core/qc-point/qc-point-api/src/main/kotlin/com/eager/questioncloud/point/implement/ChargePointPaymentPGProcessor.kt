package com.eager.questioncloud.point.implement

import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.toss.TossPaymentAPI
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentPGProcessor(
    private val tossPaymentAPI: TossPaymentAPI,
) {
    fun getPayment(orderId: String): PGPayment {
        val tossPayment = tossPaymentAPI.getPayment(orderId)
        return PGPayment(
            tossPayment.paymentKey,
            tossPayment.orderId,
            tossPayment.totalAmount,
            tossPayment.status,
        )
    }
    
    @Retryable(maxAttempts = 5)
    fun confirm(paymentId: String, orderId: String, amount: Int) {
        tossPaymentAPI.confirm(paymentId, orderId, amount)
    }
}

package com.eager.questioncloud.pg.implement

import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.toss.TossPaymentAPI
import org.springframework.stereotype.Component

@Component
class PGPaymentProcessor(
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

    fun confirm(paymentId: String, orderId: String, amount: Int) {
        tossPaymentAPI.confirm(paymentId, orderId, amount)
    }

    fun cancel(paymentId: String, amount: Int) {
        tossPaymentAPI.cancel(paymentId, amount)
    }
}

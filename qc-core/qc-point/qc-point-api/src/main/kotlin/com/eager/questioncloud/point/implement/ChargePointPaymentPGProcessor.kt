package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.common.pg.PGConfirmResponse
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PaymentAPI
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
    fun confirm(pgConfirmRequest: PGConfirmRequest): PGConfirmResponse {
        return paymentAPI.confirm(pgConfirmRequest)
    }
}

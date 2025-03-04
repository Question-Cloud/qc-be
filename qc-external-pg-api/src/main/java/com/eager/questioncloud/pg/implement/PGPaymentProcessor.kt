package com.eager.questioncloud.pg.implement

import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.portone.PortoneAPI
import org.springframework.stereotype.Component

@Component
class PGPaymentProcessor(private val portoneAPI: PortoneAPI) {
    fun getPayment(paymentId: String): PGPayment {
        val portonePayment = portoneAPI.getPayment(paymentId)
        return PGPayment(portonePayment.id, portonePayment.amount.total, portonePayment.receiptUrl)
    }

    fun cancel(paymentId: String) {
        portoneAPI.cancel(paymentId)
    }
}

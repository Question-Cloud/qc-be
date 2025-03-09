package com.eager.questioncloud.application.business.payment.point.service

import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import org.springframework.stereotype.Component

@Component
class PgPaymentService(
    private val pgPaymentProcessor: PGPaymentProcessor,
) {
    fun getPgPayment(paymentId: String): PGPayment {
        return pgPaymentProcessor.getPayment(paymentId)
    }
}
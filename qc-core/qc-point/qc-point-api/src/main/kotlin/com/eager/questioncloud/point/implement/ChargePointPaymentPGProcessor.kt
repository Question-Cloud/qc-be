package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.common.pg.PGConfirmResult
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PaymentAPI
import org.springframework.retry.annotation.Recover
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
    fun confirm(pgConfirmRequest: PGConfirmRequest): PGConfirmResult {
        return paymentAPI.confirm(pgConfirmRequest)
    }
    
    @Recover
    fun recover(ex: Exception, pgConfirmRequest: PGConfirmRequest): PGConfirmResult {
        throw CoreException(Error.PAYMENT_ERROR)
    }
}

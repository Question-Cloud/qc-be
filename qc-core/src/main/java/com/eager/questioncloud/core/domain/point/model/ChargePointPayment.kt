package com.eager.questioncloud.core.domain.point.model

import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.core.exception.InvalidPaymentException
import io.hypersistence.tsid.TSID
import java.time.LocalDateTime

class ChargePointPayment(
    val orderId: String,
    var paymentId: String? = null,
    val userId: Long,
    val chargePointType: ChargePointType,
    var chargePointPaymentStatus: ChargePointPaymentStatus = ChargePointPaymentStatus.ORDERED,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var requestAt: LocalDateTime? = null,
) {
    fun prepare(paymentId: String) {
        this.paymentId = paymentId
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PAYMENT_REQUEST
        this.requestAt = LocalDateTime.now()
    }

    fun charge() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
    }

    fun cancel() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.CANCELED
    }

    fun validatePayment(paidAmount: Int) {
        validateStatus()
        validateAmount(paidAmount)
    }

    private fun validateAmount(paidAmount: Int) {
        if (chargePointType.amount != paidAmount) {
            throw InvalidPaymentException()
        }
    }

    private fun validateStatus() {
        if (chargePointPaymentStatus != ChargePointPaymentStatus.ORDERED) {
            throw CoreException(Error.ALREADY_PROCESSED_PAYMENT)
        }
    }

    companion object {
        fun createOrder(
            userId: Long,
            chargePointType: ChargePointType
        ): ChargePointPayment {
            return ChargePointPayment(
                orderId = TSID.Factory.getTsid().toString(),
                userId = userId,
                chargePointType = chargePointType
            )
        }
    }
}

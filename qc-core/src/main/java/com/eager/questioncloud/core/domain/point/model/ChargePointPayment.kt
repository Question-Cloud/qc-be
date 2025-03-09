package com.eager.questioncloud.core.domain.point.model

import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.core.exception.InvalidPaymentException
import java.time.LocalDateTime

class ChargePointPayment(
    val paymentId: String,
    val userId: Long,
    var receiptUrl: String? = null,
    val chargePointType: ChargePointType,
    var chargePointPaymentStatus: ChargePointPaymentStatus = ChargePointPaymentStatus.ORDERED,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var paidAt: LocalDateTime? = null,
) {
    fun approve(receiptUrl: String) {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PAID
        this.receiptUrl = receiptUrl
        this.paidAt = LocalDateTime.now()
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
        @JvmStatic
        fun order(
            paymentId: String,
            userId: Long,
            chargePointType: ChargePointType
        ): ChargePointPayment {
            return ChargePointPayment(
                paymentId = paymentId,
                userId = userId,
                chargePointType = chargePointType
            )
        }
    }
}

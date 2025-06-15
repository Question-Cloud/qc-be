package com.eager.questioncloud.point.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.exception.InvalidPointChargeException
import io.hypersistence.tsid.TSID
import java.time.LocalDateTime

class ChargePointPayment(
    val orderId: String = TSID.Factory.getTsid().toString(),
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

    fun failCancel() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PAYMENT_REQUEST
    }

    fun validatePayment(paidAmount: Int) {
        validateStatus()
        validateAmount(paidAmount)
    }

    private fun validateAmount(paidAmount: Int) {
        if (chargePointType.amount != paidAmount) {
            throw InvalidPointChargeException(paymentId!!)
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
                userId = userId,
                chargePointType = chargePointType
            )
        }
    }
}

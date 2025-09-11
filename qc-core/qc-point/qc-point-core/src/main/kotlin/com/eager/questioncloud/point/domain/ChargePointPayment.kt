package com.eager.questioncloud.point.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
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
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT
        this.requestAt = LocalDateTime.now()
    }
    
    fun charge() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
    }
    
    fun fail() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.FAILED
    }
    
    fun recovery(): Boolean {
        if (chargePointPaymentStatus != ChargePointPaymentStatus.PENDING_PG_PAYMENT) {
            return false
        }
        
        this.chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
        return true;
    }
    
    fun validatePayment(paidAmount: Int) {
        validateStatus()
        validateAmount(paidAmount)
    }
    
    private fun validateAmount(paidAmount: Int) {
        if (chargePointType.amount != paidAmount) {
            throw CoreException(Error.INVALID_CHARGE_POINT_PAYMENT)
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

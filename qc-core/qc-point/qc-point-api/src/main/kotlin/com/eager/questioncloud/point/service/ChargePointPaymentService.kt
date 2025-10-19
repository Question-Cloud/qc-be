package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.*
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentService(
    private val chargePointPaymentReader: ChargePointPaymentReader,
    private val chargePointPaymentPreparer: ChargePointPaymentPreparer,
    private val chargePointPaymentAppender: ChargePointPaymentAppender,
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
    private val chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor,
    private val chargePointPaymentIdempotentInfoReader: ChargePointPaymentIdempotentInfoReader
) {
    fun createOrder(userId: Long, chargePointType: ChargePointType): String {
        val order = chargePointPaymentAppender.createOrder(userId, chargePointType)
        return order.orderId
    }
    
    fun approvePayment(orderId: String): ChargePointPaymentStatus {
        val idempotentInfo = chargePointPaymentIdempotentInfoReader.getOrderId(orderId)
        
        if (idempotentInfo != null) {
            return idempotentInfo.chargePointPaymentStatus
        }
        
        val pgPayment = chargePointPaymentPGProcessor.getPayment(orderId)
        val chargePointPayment = chargePointPaymentPreparer.prepare(pgPayment)
        
        val confirmRequest = PGConfirmRequest(pgPayment.paymentId, pgPayment.orderId, pgPayment.amount)
        val confirmResult = chargePointPaymentPGProcessor.confirm(confirmRequest)
        
        return chargePointPaymentPostProcessor.postProcess(chargePointPayment, confirmResult)
    }
    
    fun isCompletePayment(userId: Long, orderId: String): Boolean {
        return chargePointPaymentReader.isCompletedPayment(userId, orderId)
    }
}

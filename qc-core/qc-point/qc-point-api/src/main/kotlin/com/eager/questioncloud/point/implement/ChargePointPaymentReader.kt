package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentReader(
    private val chargePointPaymentRepository: ChargePointPaymentRepository
) {
    fun getChargePointPayment(userId: Long, pagingInformation: PagingInformation): List<ChargePointPayment> {
        return chargePointPaymentRepository.getChargePointPayments(userId, pagingInformation)
    }
    
    fun countChargePointPayment(userId: Long): Int {
        return chargePointPaymentRepository.countByUserId(userId)
    }
    
    fun isCompletedPayment(userId: Long, orderId: String): Boolean {
        return chargePointPaymentRepository.isCompletedPayment(userId, orderId)
    }
}
package com.eager.questioncloud.point.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment

interface ChargePointPaymentRepository {
    fun save(chargePointPayment: ChargePointPayment): ChargePointPayment
    
    fun update(chargePointPayment: ChargePointPayment): ChargePointPayment
    
    fun isCompletedPayment(userId: Long, orderId: String): Boolean
    
    fun findByOrderIdWithLock(orderId: String): ChargePointPayment
    
    fun findByOrderId(orderId: String): ChargePointPayment
    
    fun getChargePointPayments(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<ChargePointPayment>
    
    fun countByUserId(userId: Long): Int
    
    fun getPendingPayments(): List<ChargePointPayment>
    
    fun deleteAllInBatch()
}

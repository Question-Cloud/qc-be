package com.eager.questioncloud.point.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment

interface ChargePointPaymentRepository {
    fun save(chargePointPayment: ChargePointPayment): ChargePointPayment

    fun update(chargePointPayment: ChargePointPayment): ChargePointPayment

    fun isCompletedPayment(userId: Long, paymentId: String): Boolean

    fun findByOrderIdWithLock(paymentId: String): ChargePointPayment

    fun findByOrderId(orderId: String): ChargePointPayment

    fun getChargePointPayments(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<ChargePointPayment>

    fun countByUserId(userId: Long): Int

    fun deleteAllInBatch()
}

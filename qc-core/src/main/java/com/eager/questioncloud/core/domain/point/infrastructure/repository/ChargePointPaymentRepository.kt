package com.eager.questioncloud.core.domain.point.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment

interface ChargePointPaymentRepository {
    fun save(chargePointPayment: ChargePointPayment): ChargePointPayment

    fun isCompletedPayment(userId: Long, paymentId: String): Boolean

    fun findByOrderIdWithLock(paymentId: String): ChargePointPayment

    fun getChargePointPayments(userId: Long, pagingInformation: PagingInformation): List<ChargePointPayment>

    fun countByUserId(userId: Long): Int

    fun deleteAllInBatch()
}

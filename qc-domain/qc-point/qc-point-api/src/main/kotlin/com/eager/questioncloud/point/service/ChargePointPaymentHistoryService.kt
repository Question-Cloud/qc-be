package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.infrastructure.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Service

@Service
class ChargePointPaymentHistoryService(
    private val chargePointPaymentRepository: ChargePointPaymentRepository
) {
    fun getChargePointPayments(userId: Long, pagingInformation: PagingInformation): List<ChargePointPayment> {
        return chargePointPaymentRepository.getChargePointPayments(userId, pagingInformation)
    }

    fun countChargePointPayment(userId: Long): Int {
        return chargePointPaymentRepository.countByUserId(userId)
    }
}

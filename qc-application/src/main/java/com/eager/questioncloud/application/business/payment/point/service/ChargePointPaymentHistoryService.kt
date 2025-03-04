package com.eager.questioncloud.application.business.payment.point.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
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

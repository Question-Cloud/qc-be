package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.dto.ChargePointPaymentHistory
import com.eager.questioncloud.point.implement.ChargePointPaymentReader
import org.springframework.stereotype.Service

@Service
class ChargePointPaymentHistoryService(
    private val chargePointPaymentReader: ChargePointPaymentReader
) {
    fun getChargePointPaymentHistory(userId: Long, pagingInformation: PagingInformation): List<ChargePointPaymentHistory> {
        val chargePointPayments = chargePointPaymentReader.getChargePointPayment(userId, pagingInformation)
        return ChargePointPaymentHistory.from(chargePointPayments)
    }
    
    fun countChargePointPayment(userId: Long): Int {
        return chargePointPaymentReader.countChargePointPayment(userId)
    }
}

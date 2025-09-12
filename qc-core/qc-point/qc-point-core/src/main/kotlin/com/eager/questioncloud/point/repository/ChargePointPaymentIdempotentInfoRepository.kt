package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo

interface ChargePointPaymentIdempotentInfoRepository {
    fun save(chargePointPaymentIdempotentInfo: ChargePointPaymentIdempotentInfo): Boolean
    fun findByOrderId(orderId: String): ChargePointPaymentIdempotentInfo?
}
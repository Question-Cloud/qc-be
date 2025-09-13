package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo

interface ChargePointPaymentIdempotentInfoRepository {
    fun insert(chargePointPaymentIdempotentInfo: ChargePointPaymentIdempotentInfo): Boolean
    fun findByOrderId(orderId: String): ChargePointPaymentIdempotentInfo?
}
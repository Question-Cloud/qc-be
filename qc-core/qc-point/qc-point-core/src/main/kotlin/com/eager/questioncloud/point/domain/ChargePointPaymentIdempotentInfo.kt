package com.eager.questioncloud.point.domain

import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import java.time.LocalDateTime

class ChargePointPaymentIdempotentInfo(
    val orderId: String,
    val paymentId: String,
    val chargePointPaymentStatus: ChargePointPaymentStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
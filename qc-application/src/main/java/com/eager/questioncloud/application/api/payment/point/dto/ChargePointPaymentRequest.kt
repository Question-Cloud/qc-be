package com.eager.questioncloud.application.api.payment.point.dto

import com.eager.questioncloud.pg.portone.PortoneWebhookStatus

class ChargePointPaymentRequest(
    val payment_id: String,
    val status: PortoneWebhookStatus,
)

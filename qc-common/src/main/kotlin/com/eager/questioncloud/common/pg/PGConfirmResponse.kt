package com.eager.questioncloud.common.pg

class PGConfirmResponse(
    val orderId: String,
    val paymentId: String,
    val status: PGPaymentStatus
)
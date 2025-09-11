package com.eager.questioncloud.common.pg

class PGPayment(
    val paymentId: String,
    val orderId: String,
    val amount: Int,
    val status: PGPaymentStatus
)
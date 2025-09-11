package com.eager.questioncloud.common.pg

class PGConfirmRequest(
    val paymentId: String,
    val orderId: String,
    val amount: Int
)
package com.eager.questioncloud.pg.toss

class TossPaymentConfirmRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
)
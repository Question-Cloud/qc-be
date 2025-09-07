package com.eager.questioncloud.pg.model

import com.eager.questioncloud.pg.toss.PaymentStatus

class PGPayment(
    val paymentId: String,
    val orderId: String,
    val amount: Int,
    val status: PaymentStatus
)
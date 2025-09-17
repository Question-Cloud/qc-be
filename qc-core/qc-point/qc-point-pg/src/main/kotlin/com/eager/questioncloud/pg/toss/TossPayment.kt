package com.eager.questioncloud.pg.toss

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class TossPayment(
    val paymentKey: String,
    val orderId: String,
    val totalAmount: Int,
    val status: TossPaymentStatus
)
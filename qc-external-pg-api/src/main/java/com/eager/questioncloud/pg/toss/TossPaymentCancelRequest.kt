package com.eager.questioncloud.pg.toss

class TossPaymentCancelRequest(
    val cancelAmount: Int,
    val cancelReason: String = "결제 시스템 예외",
)
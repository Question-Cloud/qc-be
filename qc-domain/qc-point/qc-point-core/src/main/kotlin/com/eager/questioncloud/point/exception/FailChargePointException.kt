package com.eager.questioncloud.point.exception

class FailChargePointException(
    val paymentId: String
) : RuntimeException() {
}

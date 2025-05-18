package com.eager.questioncloud.core.exception

class FailChargePointException(
    val paymentId: String
) : RuntimeException() {
}

package com.eager.questioncloud.point.exception

class InvalidPointChargeException(
    val paymentId: String
) : RuntimeException() {
}
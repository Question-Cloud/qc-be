package com.eager.questioncloud.common.message

class FailChargePointPaymentMessagePayload(
    val orderId: String,
) {
    companion object {
        fun create(orderId: String): FailChargePointPaymentMessagePayload {
            return FailChargePointPaymentMessagePayload(orderId)
        }
    }
}
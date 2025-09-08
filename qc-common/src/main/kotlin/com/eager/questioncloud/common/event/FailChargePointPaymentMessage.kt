package com.eager.questioncloud.common.event

class FailChargePointPaymentMessage(
    val orderId: String,
) {
    companion object {
        fun create(orderId: String): FailChargePointPaymentMessage {
            return FailChargePointPaymentMessage(orderId)
        }
    }
}
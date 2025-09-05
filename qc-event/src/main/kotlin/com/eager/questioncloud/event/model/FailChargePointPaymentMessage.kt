package com.eager.questioncloud.event.model

class FailChargePointPaymentMessage(
    val orderId: String,
) {
    companion object {
        fun create(orderId: String): FailChargePointPaymentMessage {
            return FailChargePointPaymentMessage(orderId)
        }
    }
}
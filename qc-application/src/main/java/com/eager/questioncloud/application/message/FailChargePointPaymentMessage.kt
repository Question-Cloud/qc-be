package com.eager.questioncloud.application.message

class FailChargePointPaymentMessage(
    var failCount: Int,
    var paymentId: String,
) {
    fun increaseFailCount() {
        failCount++
    }

    companion object {
        fun create(paymentId: String): FailChargePointPaymentMessage {
            return FailChargePointPaymentMessage(0, paymentId)
        }
    }
}

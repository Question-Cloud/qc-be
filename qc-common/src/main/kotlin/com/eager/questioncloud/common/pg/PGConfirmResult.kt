package com.eager.questioncloud.common.pg

sealed class PGConfirmResult(val orderId: String, val paymentId: String) {
    class Success(orderId: String, paymentId: String) : PGConfirmResult(orderId, paymentId)
    class Fail(orderId: String, paymentId: String) : PGConfirmResult(orderId, paymentId)
}
package com.eager.questioncloud.core.domain.point.enums

enum class ChargePointPaymentStatus(val value: String) {
    ORDERED("ORDERED"), PAYMENT_REQUEST("PAYMENT_REQUEST"), CHARGED("CHARGED"), CANCELED("CANCELED")
}

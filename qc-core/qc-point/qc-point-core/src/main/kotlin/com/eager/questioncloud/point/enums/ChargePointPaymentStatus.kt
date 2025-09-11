package com.eager.questioncloud.point.enums

enum class ChargePointPaymentStatus(val value: String) {
    ORDERED("ORDERED"), PENDING_PG_PAYMENT("PENDING_PG_PAYMENT"), CHARGED("CHARGED"), CANCELED("CANCELED"), FAILED("FAILED")
}

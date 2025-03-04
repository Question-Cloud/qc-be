package com.eager.questioncloud.pg.portone

enum class PortonePaymentStatus(val value: String) {
    READY("READY"), CANCELLED("CANCELLED"), FAILED("FAILED"), PAID("PAID"), PARTIAL_CANCELLED("PARTIAL_CANCELLED")
}

package com.eager.questioncloud.common.pg.domain

enum class PGPaymentStatus(val status: String) {
    READY("READY"), IN_PROGRESS("IN_PROGRESS"), WAITING_FOR_DEPOSIT("WAITING_FOR_DEPOSIT"),
    DONE("DONE"), CANCELED("CANCELED"), PARTIAL_CANCELED("PARTIAL_CANCELED"),
    ABORTED("ABORTED"), EXPIRED("EXPIRED")
}
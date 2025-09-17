package com.eager.questioncloud.pg.toss

enum class TossPaymentStatus(val status: String) {
    READY("READY"), IN_PROGRESS("IN_PROGRESS"), WAITING_FOR_DEPOSIT("WAITING_FOR_DEPOSIT"),
    DONE("DONE"), CANCELED("CANCELED"), PARTIAL_CANCELED("PARTIAL_CANCELED"),
    ABORTED("ABORTED"), EXPIRED("EXPIRED")
}
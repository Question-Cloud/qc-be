package com.eager.questioncloud.pg.portone

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PortonePayment(
    val id: String,
    val status: PortonePaymentStatus,
    val amount: PortonePaymentAmount,
    val receiptUrl: String,
)

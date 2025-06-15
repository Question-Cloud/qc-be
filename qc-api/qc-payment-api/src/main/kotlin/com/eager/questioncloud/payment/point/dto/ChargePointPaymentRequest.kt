package com.eager.questioncloud.payment.point.dto

import com.fasterxml.jackson.annotation.JsonProperty

class ChargePointPaymentRequest(
    @JsonProperty("orderId")
    val orderId: String
)

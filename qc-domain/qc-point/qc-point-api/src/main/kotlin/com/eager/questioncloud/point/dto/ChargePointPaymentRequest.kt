package com.eager.questioncloud.point.dto

import com.fasterxml.jackson.annotation.JsonProperty

class ChargePointPaymentRequest(
    @JsonProperty("orderId")
    val orderId: String
)

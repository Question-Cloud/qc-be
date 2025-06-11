package com.eager.questioncloud.application.api.payment.point.dto

import com.fasterxml.jackson.annotation.JsonProperty

class CheckCompletePaymentResponse(
    @JsonProperty("isCompleted")
    val isCompleted: Boolean
)

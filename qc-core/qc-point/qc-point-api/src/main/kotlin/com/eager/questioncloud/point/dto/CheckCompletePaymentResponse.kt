package com.eager.questioncloud.point.dto

import com.fasterxml.jackson.annotation.JsonProperty

class CheckCompletePaymentResponse(
    @JsonProperty("isCompleted")
    val isCompleted: Boolean
)

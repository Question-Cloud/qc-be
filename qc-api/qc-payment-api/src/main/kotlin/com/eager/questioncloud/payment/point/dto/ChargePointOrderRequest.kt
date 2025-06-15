package com.eager.questioncloud.payment.point.dto

import com.eager.questioncloud.point.enums.ChargePointType
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

class ChargePointOrderRequest(
    @JsonProperty("chargePointType")
    @NotNull val chargePointType: ChargePointType
)
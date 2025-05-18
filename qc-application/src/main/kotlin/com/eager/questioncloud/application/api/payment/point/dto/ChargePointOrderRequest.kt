package com.eager.questioncloud.application.api.payment.point.dto

import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

class ChargePointOrderRequest(
    @JsonProperty("chargePointType")
    @NotNull val chargePointType: ChargePointType
)
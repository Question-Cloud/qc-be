package com.eager.questioncloud.application.api.payment.point.dto

import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ChargePointOrderRequest(
    @NotNull val chargePointType: ChargePointType,
    @NotBlank val paymentId: String,
)
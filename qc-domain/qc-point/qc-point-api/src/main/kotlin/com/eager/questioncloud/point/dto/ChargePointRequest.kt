package com.eager.questioncloud.point.dto

import com.eager.questioncloud.point.enums.ChargePointType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ChargePointRequest(
    @NotNull val chargePointType: ChargePointType,
    @NotBlank val paymentId: String,
)
package com.eager.questioncloud.point.dto

import com.eager.questioncloud.point.enums.ChargePointType
import jakarta.validation.constraints.NotNull

class ChargePointOrderRequest(
    @NotNull val chargePointType: ChargePointType
)
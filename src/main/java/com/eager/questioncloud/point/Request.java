package com.eager.questioncloud.point;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class Request {
    @Getter
    public static class ChargePointRequest {
        private ChargePointType chargePointType;

        @NotBlank
        private String paymentId;
    }
}

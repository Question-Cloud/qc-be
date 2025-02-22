package com.eager.questioncloud.application.api.payment.point.dto;

import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.pg.portone.PortoneWebhookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ChargePointPaymentControllerRequest {
    @Getter
    public static class ChargePointRequest {
        @NotNull
        private ChargePointType chargePointType;

        @NotBlank
        private String paymentId;
    }

    @Getter
    public static class ChargePointOrderRequest {
        @NotNull
        private ChargePointType chargePointType;

        @NotBlank
        private String paymentId;
    }

    @Getter
    public static class ChargePointPaymentRequest {
        private String payment_id;
        private PortoneWebhookStatus status;
    }
}

package com.eager.questioncloud.domain.point.dto;

import com.eager.questioncloud.domain.point.vo.ChargePointType;
import com.eager.questioncloud.domain.portone.enums.PortoneWebhookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Request {
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

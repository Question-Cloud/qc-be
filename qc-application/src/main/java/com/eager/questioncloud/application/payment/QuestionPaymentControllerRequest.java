package com.eager.questioncloud.application.payment;

import com.eager.questioncloud.domain.point.ChargePointType;
import com.eager.questioncloud.pg.portone.PortoneWebhookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class QuestionPaymentControllerRequest {
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

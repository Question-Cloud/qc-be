package com.eager.questioncloud.application.api.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChargePointPaymentControllerResponse {

    @Getter
    @AllArgsConstructor
    public static class CheckCompletePaymentResponse {
        private Boolean isCompleted;
    }
}

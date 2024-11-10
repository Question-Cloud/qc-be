package com.eager.questioncloud.api.payment.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {

    @Getter
    @AllArgsConstructor
    public static class CheckCompletePaymentResponse {
        private Boolean isCompleted;
    }
}

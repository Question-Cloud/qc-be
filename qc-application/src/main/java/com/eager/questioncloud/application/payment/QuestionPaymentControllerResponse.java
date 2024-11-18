package com.eager.questioncloud.application.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionPaymentControllerResponse {

    @Getter
    @AllArgsConstructor
    public static class CheckCompletePaymentResponse {
        private Boolean isCompleted;
    }
}

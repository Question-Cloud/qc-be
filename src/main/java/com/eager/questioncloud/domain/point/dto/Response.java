package com.eager.questioncloud.domain.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class GetUserPointResponse {
        private int point;
    }

    @Getter
    @AllArgsConstructor
    public static class CheckCompletePaymentResponse {
        private Boolean isCompleted;
    }
}

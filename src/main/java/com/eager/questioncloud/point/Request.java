package com.eager.questioncloud.point;

import lombok.Getter;

public class Request {
    @Getter
    public static class ChargePointRequest {
        private ChargePointType chargePointType;
        private String paymentId;
    }
}

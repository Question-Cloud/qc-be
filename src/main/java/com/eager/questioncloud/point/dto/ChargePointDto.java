package com.eager.questioncloud.point.dto;

import com.eager.questioncloud.point.model.ChargePointOrder;
import com.eager.questioncloud.point.model.ChargePointPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChargePointDto {
    @Getter
    @AllArgsConstructor
    public static class ChargePointPaymentResult {
        private ChargePointOrder chargePointOrder;
        private ChargePointPayment chargePointPayment;
    }
}

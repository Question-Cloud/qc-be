package com.eager.questioncloud.domain.point.dto;

import com.eager.questioncloud.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.domain.point.model.ChargePointOrder;
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

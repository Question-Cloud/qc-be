package com.eager.questioncloud.core.domain.user.dto;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointOrder;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargePointPaymentResult {
    private ChargePointOrder chargePointOrder;
    private ChargePointPayment chargePointPayment;
}
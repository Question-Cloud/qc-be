package com.eager.questioncloud.core.domain.point.event;

import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.vo.ChargePointType;
import lombok.Getter;

@Getter
public class ChargePointEvent {
    private final String paymentId;
    private final Long userId;
    private final ChargePointType chargePointType;

    private ChargePointEvent(String paymentId, Long userId, ChargePointType chargePointType) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.chargePointType = chargePointType;
    }

    public static ChargePointEvent from(ChargePointPayment chargePointPayment) {
        return new ChargePointEvent(chargePointPayment.getPaymentId(), chargePointPayment.getUserId(), chargePointPayment.getChargePointType());
    }
}

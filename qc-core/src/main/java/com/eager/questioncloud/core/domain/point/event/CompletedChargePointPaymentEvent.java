package com.eager.questioncloud.core.domain.point.event;

import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import lombok.Getter;

@Getter
public class CompletedChargePointPaymentEvent {
    private final String paymentId;
    private final Long userId;
    private final ChargePointType chargePointType;

    private CompletedChargePointPaymentEvent(String paymentId, Long userId, ChargePointType chargePointType) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.chargePointType = chargePointType;
    }

    public static CompletedChargePointPaymentEvent from(ChargePointPayment chargePointPayment) {
        return new CompletedChargePointPaymentEvent(chargePointPayment.getPaymentId(), chargePointPayment.getUserId(),
            chargePointPayment.getChargePointType());
    }
}

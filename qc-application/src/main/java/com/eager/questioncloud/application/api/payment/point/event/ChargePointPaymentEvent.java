package com.eager.questioncloud.application.api.payment.point.event;

import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import lombok.Getter;

@Getter
public class ChargePointPaymentEvent {
    private final String paymentId;
    private final Long userId;
    private final ChargePointType chargePointType;

    private ChargePointPaymentEvent(String paymentId, Long userId, ChargePointType chargePointType) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.chargePointType = chargePointType;
    }

    public static ChargePointPaymentEvent from(ChargePointPayment chargePointPayment) {
        return new ChargePointPaymentEvent(chargePointPayment.getPaymentId(), chargePointPayment.getUserId(),
            chargePointPayment.getChargePointType());
    }
}

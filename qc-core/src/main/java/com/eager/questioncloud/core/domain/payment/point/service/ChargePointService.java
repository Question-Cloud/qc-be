package com.eager.questioncloud.core.domain.payment.point.service;

import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.domain.portone.implement.PortoneAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointService {
    private final ChargePointPaymentProcessor chargePointPaymentProcessor;
    private final UserPointManager userPointManager;
    private final PortoneAPI portoneAPI;

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentProcessor.createOrder(chargePointPayment);
    }

    public void paymentAndCharge(String paymentId) {
        PortonePayment portonePayment = portoneAPI.getPaymentResult(paymentId);
        ChargePointPayment chargePointPayment = chargePointPaymentProcessor.payment(portonePayment);
        userPointManager.chargePoint(
            chargePointPayment.getUserId(),
            chargePointPayment.getChargePointType()
        );
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentProcessor.isCompletePayment(userId, paymentId);
    }
}

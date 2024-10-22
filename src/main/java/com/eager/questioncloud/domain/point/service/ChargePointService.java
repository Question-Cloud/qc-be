package com.eager.questioncloud.domain.point.service;

import com.eager.questioncloud.domain.point.dto.ChargePointDto.ChargePointPaymentResult;
import com.eager.questioncloud.domain.point.implement.ChargePointOrderAppender;
import com.eager.questioncloud.domain.point.implement.ChargePointPaymentProcessor;
import com.eager.questioncloud.domain.point.implement.ChargePointPaymentReader;
import com.eager.questioncloud.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.domain.point.implement.UserPointProcessor;
import com.eager.questioncloud.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.domain.portone.implement.PortoneAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointService {
    private final ChargePointOrderAppender chargePointOrderAppender;
    private final ChargePointPaymentProcessor chargePointPaymentProcessor;
    private final ChargePointPaymentReader chargePointPaymentReader;
    private final UserPointProcessor userPointProcessor;
    private final PortoneAPI portoneAPI;

    public ChargePointOrder createOrder(ChargePointOrder chargePointOrder) {
        return chargePointOrderAppender.append(chargePointOrder);
    }

    public void paymentAndCharge(String paymentId) {
        PortonePayment portonePayment = portoneAPI.getPaymentResult(paymentId);
        ChargePointPaymentResult chargePointPaymentResult = chargePointPaymentProcessor.payment(portonePayment);
        userPointProcessor.chargePoint(
            chargePointPaymentResult.getChargePointPayment().getUserId(),
            chargePointPaymentResult.getChargePointOrder().getChargePointType());
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentReader.isCompletePayment(userId, paymentId);
    }
}

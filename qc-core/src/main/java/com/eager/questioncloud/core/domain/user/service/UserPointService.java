package com.eager.questioncloud.core.domain.user.service;

import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.domain.portone.implement.PortoneAPI;
import com.eager.questioncloud.core.domain.user.dto.ChargePointPaymentResult;
import com.eager.questioncloud.core.domain.user.implement.ChargePointOrderAppender;
import com.eager.questioncloud.core.domain.user.implement.ChargePointPaymentProcessor;
import com.eager.questioncloud.core.domain.user.implement.ChargePointPaymentReader;
import com.eager.questioncloud.core.domain.user.implement.UserPointProcessor;
import com.eager.questioncloud.core.domain.user.implement.UserPointReader;
import com.eager.questioncloud.core.domain.user.model.ChargePointOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointReader userPointReader;
    private final ChargePointOrderAppender chargePointOrderAppender;
    private final ChargePointPaymentProcessor chargePointPaymentProcessor;
    private final ChargePointPaymentReader chargePointPaymentReader;
    private final UserPointProcessor userPointProcessor;
    private final PortoneAPI portoneAPI;

    public int getUserPoint(Long userId) {
        return userPointReader.getUserPoint(userId);
    }


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

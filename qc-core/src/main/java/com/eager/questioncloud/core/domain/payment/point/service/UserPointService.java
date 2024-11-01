package com.eager.questioncloud.core.domain.payment.point.service;

import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentAppender;
import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentReader;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointProcessor;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointReader;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.domain.portone.implement.PortoneAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointReader userPointReader;
    private final ChargePointPaymentAppender chargePointPaymentAppender;
    private final ChargePointPaymentProcessor chargePointPaymentProcessor;
    private final ChargePointPaymentReader chargePointPaymentReader;
    private final UserPointProcessor userPointProcessor;
    private final PortoneAPI portoneAPI;

    public int getUserPoint(Long userId) {
        return userPointReader.getUserPoint(userId);
    }

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentAppender.append(chargePointPayment);
    }

    public void paymentAndCharge(String paymentId) {
        PortonePayment portonePayment = portoneAPI.getPaymentResult(paymentId);
        ChargePointPayment chargePointPayment = chargePointPaymentProcessor.payment(portonePayment);
        userPointProcessor.chargePoint(
            chargePointPayment.getUserId(),
            chargePointPayment.getChargePointType()
        );
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentReader.isCompletePayment(userId, paymentId);
    }
}

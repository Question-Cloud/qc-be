package com.eager.questioncloud.core.domain.payment.point.service;

import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentReader;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointEvent;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.domain.portone.implement.PortoneAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentService {
    private final ChargePointPaymentProcessor chargePointPaymentProcessor;
    private final ChargePointPaymentReader chargePointPaymentReader;
    private final PortoneAPI portoneAPI;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentProcessor.createOrder(chargePointPayment);
    }

    public void approvePayment(String paymentId) {
        PortonePayment portonePayment = portoneAPI.getPaymentResult(paymentId);
        ChargePointPayment chargePointPayment = chargePointPaymentReader.getChargePointPayment(paymentId);
        chargePointPaymentProcessor.approve(chargePointPayment, portonePayment);
        applicationEventPublisher.publishEvent(ChargePointEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentProcessor.isCompletePayment(userId, paymentId);
    }
}

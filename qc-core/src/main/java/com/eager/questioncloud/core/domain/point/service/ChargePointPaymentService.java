package com.eager.questioncloud.core.domain.point.service;

import com.eager.questioncloud.core.domain.point.event.ChargePointEvent;
import com.eager.questioncloud.core.domain.point.implement.ChargePointPaymentApprover;
import com.eager.questioncloud.core.domain.point.implement.ChargePointPaymentCreator;
import com.eager.questioncloud.core.domain.point.implement.ChargePointPaymentReader;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentService {
    private final ChargePointPaymentApprover chargePointPaymentApprover;
    private final ChargePointPaymentReader chargePointPaymentReader;
    private final ChargePointPaymentCreator chargePointPaymentCreator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentCreator.createOrder(chargePointPayment);
    }

    public void approvePayment(String paymentId) {
        ChargePointPayment chargePointPayment = chargePointPaymentApprover.approve(paymentId);
        applicationEventPublisher.publishEvent(ChargePointEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentReader.isCompletedPayment(userId, paymentId);
    }
}

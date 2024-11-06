package com.eager.questioncloud.core.domain.payment.point.service;

import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentApprover;
import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentCreator;
import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentReader;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointEvent;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.pg.PGAPI;
import com.eager.questioncloud.core.domain.pg.PGPayment;
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
    private final PGAPI pgAPI;

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentCreator.createOrder(chargePointPayment);
    }

    public void approvePayment(String paymentId) {
        PGPayment pgPayment = pgAPI.getPayment(paymentId);
        ChargePointPayment chargePointPayment = chargePointPaymentReader.getNotApproveChargePointPayment(paymentId);
        chargePointPaymentApprover.approve(chargePointPayment, pgPayment);
        applicationEventPublisher.publishEvent(ChargePointEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentReader.isCompletedPayment(userId, paymentId);
    }
}

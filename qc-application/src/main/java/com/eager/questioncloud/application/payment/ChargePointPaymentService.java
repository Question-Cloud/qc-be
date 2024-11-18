package com.eager.questioncloud.application.payment;

import com.eager.questioncloud.domain.point.ChargePointPayment;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
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
    private final LockManager lockManager;

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentCreator.createOrder(chargePointPayment);
    }

    //TODO Event 처리
    public void approvePayment(String paymentId) {
        ChargePointPayment chargePointPayment = lockManager.executeWithLock(
            LockKeyGenerator.generateChargePointPaymentKey(paymentId),
            () -> chargePointPaymentApprover.approve(paymentId)
        );
//        applicationEventPublisher.publishEvent(ChargePointEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentReader.isCompletedPayment(userId, paymentId);
    }
}

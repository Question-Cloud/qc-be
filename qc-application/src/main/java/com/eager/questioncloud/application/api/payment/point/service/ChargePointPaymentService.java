package com.eager.questioncloud.application.api.payment.point.service;

import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentApprover;
import com.eager.questioncloud.core.domain.point.event.ChargePointEvent;
import com.eager.questioncloud.core.domain.point.infrastructure.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentService {
    private final ChargePointPaymentApprover chargePointPaymentApprover;
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LockManager lockManager;

    public void createOrder(ChargePointPayment chargePointPayment) {
        if (isAlreadyCreatedOrder(chargePointPayment.getPaymentId())) {
            throw new CustomException(Error.ALREADY_ORDERED);
        }
        chargePointPaymentRepository.save(chargePointPayment);
    }

    public void approvePayment(String paymentId) {
        ChargePointPayment chargePointPayment = lockManager.executeWithLock(
            LockKeyGenerator.generateChargePointPaymentKey(paymentId),
            () -> chargePointPaymentApprover.approve(paymentId)
        );
        applicationEventPublisher.publishEvent(ChargePointEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }

    private Boolean isAlreadyCreatedOrder(String paymentId) {
        return chargePointPaymentRepository.existsByPaymentId(paymentId);
    }
}

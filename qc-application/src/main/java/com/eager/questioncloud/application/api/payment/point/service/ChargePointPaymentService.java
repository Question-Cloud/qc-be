package com.eager.questioncloud.application.api.payment.point.service;

import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentApprover;
import com.eager.questioncloud.application.api.payment.point.implement.PGAPI;
import com.eager.questioncloud.core.domain.point.dto.PGPayment;
import com.eager.questioncloud.core.domain.point.event.CompletedChargePointPaymentEvent;
import com.eager.questioncloud.core.domain.point.infrastructure.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentService {
    private final ChargePointPaymentApprover chargePointPaymentApprover;
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGAPI pgAPI;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void createOrder(ChargePointPayment chargePointPayment) {
        if (isAlreadyCreatedOrder(chargePointPayment.getPaymentId())) {
            throw new CoreException(Error.ALREADY_ORDERED);
        }
        chargePointPaymentRepository.save(chargePointPayment);
    }

    public void approvePayment(String paymentId) {
        PGPayment pgPayment = pgAPI.getPayment(paymentId);
        ChargePointPayment chargePointPayment = chargePointPaymentApprover.approve(pgPayment);
        applicationEventPublisher.publishEvent(CompletedChargePointPaymentEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }

    private Boolean isAlreadyCreatedOrder(String paymentId) {
        return chargePointPaymentRepository.existsByPaymentId(paymentId);
    }
}

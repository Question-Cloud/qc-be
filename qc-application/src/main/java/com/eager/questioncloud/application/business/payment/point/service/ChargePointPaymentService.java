package com.eager.questioncloud.application.business.payment.point.service;

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent;
import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentApprover;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.pg.dto.PGPayment;
import com.eager.questioncloud.pg.implement.PGPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentService {
    private final ChargePointPaymentApprover chargePointPaymentApprover;
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGPaymentProcessor pgPaymentProcessor;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void createOrder(ChargePointPayment chargePointPayment) {
        chargePointPaymentRepository.save(chargePointPayment);
    }

    public void approvePayment(String paymentId) {
        PGPayment pgPayment = pgPaymentProcessor.getPayment(paymentId);
        ChargePointPayment chargePointPayment = chargePointPaymentApprover.approve(pgPayment);
        applicationEventPublisher.publishEvent(ChargePointPaymentEvent.from(chargePointPayment));
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }
}

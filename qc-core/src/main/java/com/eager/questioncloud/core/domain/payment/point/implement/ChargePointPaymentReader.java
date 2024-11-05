package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentReader {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public ChargePointPayment getChargePointPayment(String paymentId) {
        return chargePointPaymentRepository.findByPaymentId(paymentId);
    }

    public Boolean isCompletedPayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }
}

package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentReader {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public Boolean isCompletedPayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }
}

package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentReader {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.existsByUserIdAndPaymentId(userId, paymentId);
    }
}

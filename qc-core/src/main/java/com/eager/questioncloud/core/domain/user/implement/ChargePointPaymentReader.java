package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.repository.ChargePointPaymentRepository;
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

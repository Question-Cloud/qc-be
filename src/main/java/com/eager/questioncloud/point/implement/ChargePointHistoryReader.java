package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointHistoryReader {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public Boolean existsById(String paymentId) {
        return chargePointPaymentRepository.existsById(paymentId);
    }
}

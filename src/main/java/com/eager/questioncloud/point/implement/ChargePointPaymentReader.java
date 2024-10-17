package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentReader {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public Boolean existsById(String paymentId) {
        return chargePointPaymentRepository.existsById(paymentId);
    }
}

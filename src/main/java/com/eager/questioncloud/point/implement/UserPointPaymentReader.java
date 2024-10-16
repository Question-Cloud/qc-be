package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.repository.ChargePointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointPaymentReader {
    private final ChargePointHistoryRepository chargePointHistoryRepository;

    public Boolean existsById(String paymentId) {
        return chargePointHistoryRepository.existsById(paymentId);
    }
}

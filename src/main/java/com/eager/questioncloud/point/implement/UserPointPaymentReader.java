package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.repository.UserPointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointPaymentReader {
    private final UserPointPaymentRepository userPointPaymentRepository;

    public Boolean existsById(String paymentId) {
        return userPointPaymentRepository.existsById(paymentId);
    }
}

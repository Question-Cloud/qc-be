package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.user.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentAppender {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public ChargePointPayment append(ChargePointPayment chargePointPayment) {
        return chargePointPaymentRepository.save(chargePointPayment);
    }
}
